package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.dto.BranchDto;
import com.example.NeoCafe.dto.DeletedDTO;
import com.example.NeoCafe.entity.BranchTimeTable;
import com.example.NeoCafe.entity.Branches;
import com.example.NeoCafe.repository.BranchTimeTableRepository;
import com.example.NeoCafe.repository.BranchesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchesService {

    private final BranchesRepository branchesRepository;

    private final BranchTimeTableRepository branchTimeTableRepository;

    public BranchesService(BranchesRepository branchesRepository, BranchTimeTableRepository branchTimeTableRepository) {
        this.branchesRepository = branchesRepository;
        this.branchTimeTableRepository = branchTimeTableRepository;
    }

    public List<Branches> getAllForClient(){
        return branchesRepository.findAllByStatus(Status.ACTIVATE);
    }

    public List<Branches> getAll(){
        List<Branches> branches = branchesRepository.findAllByStatus(Status.ACTIVATE);
        return branches;
    }

    public Branches addBranch(BranchDto branchDto) throws Exception {
        if (branchesRepository.existsByName(branchDto.getName())){
            throw new Exception("такой филиал уже существует!");
        }
        else {
            Branches branch = new Branches();
            branch.setName(branchDto.getName());
            branch.setLink2gis(branchDto.getLink2gis());
            branch.setAddress(branchDto.getAddress());
            branch.setPhoneNumber(branchDto.getPhoneNumber());
            branch.setStatus(Status.ACTIVATE);

            return getBranches(branchDto, branch);
        }
    }

    public DeletedDTO deleted (Long id) throws Exception {
        Branches branch = branchesRepository.findById(id).orElseThrow(
                ()-> new Exception("филиал с такой id не существует! id = "+ id)
        );
        branch.setName("deleted name: "+branch.getName());
        branch.setStatus(Status.DELETED);
        branchesRepository.save(branch);
        return new DeletedDTO(id);
    }

    public Branches update (BranchDto branchDto) throws Exception {
        Branches branches = branchesRepository.findById(branchDto.getId()).orElseThrow(
                ()-> new Exception("филиал с такой id не существует! id = "+ branchDto.getId())
        );
        branches.setName(branchDto.getName());
        branches.setLink2gis(branchDto.getLink2gis());
        branches.setAddress(branchDto.getAddress());
        branches.setPhoneNumber(branchDto.getPhoneNumber());

        return getBranches(branchDto, branches);
    }

    private Branches getBranches(BranchDto branchDto, Branches branches) {
        BranchTimeTable branchTimeTable = new BranchTimeTable();
        branchTimeTable.setSunday(branchDto.getBranchTimeDTO().getSunday());
        branchTimeTable.setMonday(branchDto.getBranchTimeDTO().getMonday());
        branchTimeTable.setThursday(branchDto.getBranchTimeDTO().getThursday());
        branchTimeTable.setWednesday(branchDto.getBranchTimeDTO().getWednesday());
        branchTimeTable.setTuesday(branchDto.getBranchTimeDTO().getTuesday());
        branchTimeTable.setFriday(branchDto.getBranchTimeDTO().getFriday());
        branchTimeTable.setSaturday(branchDto.getBranchTimeDTO().getSaturday());

        branches.setWorkingTime(branchTimeTableRepository.save(branchTimeTable));

        return  branchesRepository.save(branches);
    }
}
