package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.ERole;
import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.dto.*;
import com.example.NeoCafe.entity.*;
import com.example.NeoCafe.exception.ResourceNotFoundException;
import com.example.NeoCafe.repository.TimeTableRepository;
import com.example.NeoCafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final TimeTableRepository timeTableRepository;

    public UserDetailsServiceImpl(PasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TimeTableRepository timeTableRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.timeTableRepository = timeTableRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }

    public void activate(UserDto userDto) throws Exception {
        User user = userRepository.findByPhoneNumber(userDto.getPhoneNumber()).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));
        if (bCryptPasswordEncoder.matches(userDto.getCode(), user.getActivationCode())) {
            user.setActive(true);
            user.setCompleted(true);
            user.setStatus(Status.ACTIVATE);
        }
        else
            throw new Exception("Неправильный код!");
        userRepository.save(user);
    }

    public boolean auth(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));
        return user.isActive();
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean checkPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));
        if (!user.isActive()){
            userRepository.delete(user);
        }
        return true;
    }

    public User getByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким номером не существует!"));
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        return getByPhoneNumber(phoneNumber);
    }

    @Transactional
    public DataDto updateData(DataDto dto) throws Exception {
        try {
            User user = getCurrentUser();
            user.setFirstName(dto.getFirstname());
            user.setLastName(dto.getLastname());
            user.setBDate(dto.getBDate());
            user.setCompleted(true);
            userRepository.save(user);
            return dto;
        }
        catch (Exception e){
            throw new Exception("Ошибка при обновлении!");
        }
    }

    public CheckCompletedDTO checkCompleted(){
        User user = getCurrentUser();
        return new CheckCompletedDTO(user.isCompleted());
    }


    @Transactional
    public void updateDataClient(ClientDataDto dto) throws Exception {
        try {
            User user = getCurrentUser();
            user.setFirstName(dto.getFirstName());
            user.setBDate(dto.getBDate());
            user.setCompleted(true);
            userRepository.save(user);
        }
        catch (Exception e){
            throw new Exception("Ошибка при обновлении!");
        }
    }

    public void setBDateClient(Date date) throws Exception {
        try {
            User user = getCurrentUser();
            user.setBDate(date);
            userRepository.save(user);
        }catch (Exception e){
            throw new Exception("Ошибка при сохранении!");
        }
    }

    public TimeTable getTimeTable(){
        User user  = getCurrentUser();
        long idTimeTable = user.getTime().getTimeId();

        return timeTableRepository.findById(idTimeTable).orElseThrow(
                () -> new ResourceNotFoundException("нет расписания с таким id = ",idTimeTable));

    }

    public EmployeePersonalDataDTO getPersonalDate(){
        User user = getCurrentUser();
        EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
        employeePersonalDataDTO.setFirstName(user.getFirstName());
        employeePersonalDataDTO.setLastName(user.getLastName());
        employeePersonalDataDTO.setPhoneNumber(user.getPhoneNumber());
        employeePersonalDataDTO.setBDate(user.getBDate());
        employeePersonalDataDTO.setTimeTable(timeTableRepository.findById(user.getTime().getTimeId()).orElseThrow(
                ()-> new ResourceNotFoundException("У текущего сотрудника нету расписания!")
        ));
        return employeePersonalDataDTO;
    }

    public RatingDto getTopRaiting() {

        User user = getCurrentUser();
        List<User> userList = userRepository.
                findAllByStatusAndBranchesAndRoleOrderByRatingDesc(Status.ACTIVATE,
                        user.getBranches(), ERole.ROLE_WAITER);
        int counter = 0;
        for (User u : userList) {
            counter++;
            if (u.getId() == user.getId()) {
                return new RatingDto(counter);
            }
        }
        return new RatingDto(-1);
    }

    public ClientInfoDto getPersonalData(){
        User user = getCurrentUser();
        ClientInfoDto dto = new ClientInfoDto();
        dto.setId(user.getId());
        dto.setName(user.getFirstName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBDate(user.getBDate());
        return dto;
    }
}