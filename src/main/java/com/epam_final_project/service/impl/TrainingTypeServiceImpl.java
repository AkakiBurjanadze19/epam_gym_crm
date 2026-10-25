package com.epam_final_project.service.impl;

import com.epam_final_project.dao.TrainingTypeDao;
import com.epam_final_project.domain.TrainingType;
import com.epam_final_project.dto.trainingtype.response.TrainingTypeResponseDto;
import com.epam_final_project.mapper.TrainingTypeMapper;
import com.epam_final_project.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeDao trainingTypeDao;
    private TrainingTypeMapper trainingTypeMapper;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeDao trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Autowired
    public void setTrainingTypeMapper(TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingType> selectTrainingType(Integer trainingTypeId) {
        return trainingTypeDao.findById(trainingTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeResponseDto> getAllTrainingTypes() {
        return trainingTypeDao
                .findAll()
                .stream()
                .map(trainingTypeMapper::toDto)
                .toList();
    }
}