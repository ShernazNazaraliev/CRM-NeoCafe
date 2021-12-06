package com.example.NeoCafe.service;

import com.example.NeoCafe.Enums.Status;
import com.example.NeoCafe.Enums.TableStatus;
import com.example.NeoCafe.dto.DeletedDTO;
import com.example.NeoCafe.dto.TableDTO;
import com.example.NeoCafe.dto.TableDtoForAdmin;
import com.example.NeoCafe.dto.TablesDtoForWaiter;
import com.example.NeoCafe.entity.*;
import com.example.NeoCafe.exception.AlreadyExistsException;
import com.example.NeoCafe.exception.ResourceNotFoundException;
import com.example.NeoCafe.exception.TableNotFoundException;
import com.example.NeoCafe.repository.BranchesRepository;
import com.example.NeoCafe.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;
    
    private final BranchesRepository branchesRepository;

    private final UserDetailsServiceImpl userDetailsService;

    public TableService(TableRepository tableRepository, BranchesRepository branchesRepository, UserDetailsServiceImpl userDetailsService) {
        this.tableRepository = tableRepository;
        this.branchesRepository = branchesRepository;
        this.userDetailsService = userDetailsService;
    }

    public List<TableDtoForAdmin> getAll(){
        List<Tables> tablesList = tableRepository.findAllByStatus(Status.ACTIVATE);
        List<TableDtoForAdmin> result = new ArrayList<>();
        for (Tables t: tablesList) {
            TableDtoForAdmin tableDtoForAdmin = new TableDtoForAdmin();
            tableDtoForAdmin.setId(t.getId());
            tableDtoForAdmin.setNameBranch(t.getBranches().getName());
            if (t.getUser()!=null){
                tableDtoForAdmin.setNameEmployee(t.getUser().getFirstName());
            }
            result.add(tableDtoForAdmin);
        }
        return result;
    }

    public TableDTO add(TableDTO tableDTO) throws AlreadyExistsException {
        if (!tableRepository.existsByQrCode(tableDTO.getQrCode())) {
            Tables table = new Tables();
            table.setTableStatus(TableStatus.FREE);
            table.setQrCode(tableDTO.getQrCode());
            table.setStatus(Status.ACTIVATE);
            table.setBranches(branchesRepository.findById(tableDTO.getBranchId()).orElseThrow(
                    () -> new ResourceNotFoundException("Филиал с такой id не существует! id = ", tableDTO.getBranchId())
            ));
            tableRepository.save(table);
            return tableDTO;
        }
        else
            throw new AlreadyExistsException("такой qr_code уже существует!");
    }

    public List<TablesDtoForWaiter> allTablesByUser(){
        List<Tables> list = tableRepository.findAllByUser(userDetailsService.getCurrentUser());
        List<TablesDtoForWaiter> resultList = new ArrayList<>();

        for(Tables tables: list){
            TablesDtoForWaiter model = new TablesDtoForWaiter();
            model.setTableId(tables.getId());
            model.setStatus(tables.getTableStatus());
            resultList.add(model);
        }
        return resultList;
    }

    public DeletedDTO delete(Long id){
        Tables tables = tableRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Стол с такой id не найден! id = ",id)
        );
        tables.setStatus(Status.DELETED);
        tables.setQrCode("delete qr_code: "+ tables.getQrCode());
        return new DeletedDTO(id);
    }

    public Tables reserve(Long id) throws Exception{
        return tableRepository.findById(id)
                .map(reserveTable -> {
                    reserveTable.setTableStatus(TableStatus.OCCUPIED);
                    return tableRepository.save(reserveTable);
                }).orElseThrow( Exception::new);
    }

    public Tables freeUpTable(Long id) throws Exception{
        return tableRepository.findById(id)
                .map(freeUp -> {
                    freeUp.setTableStatus(TableStatus.FREE);
                    return tableRepository.save(freeUp);
                }).orElseThrow( Exception :: new);
    }

    public boolean isFree(Long tableId) throws Exception {
        Optional<Tables> table = tableRepository.findById(tableId);
        if(table.isPresent()){
            TableStatus status = tableRepository.findById(tableId).get().getTableStatus();
            return status == TableStatus.FREE;
        }else {
            throw new TableNotFoundException();
        }
    }

}
