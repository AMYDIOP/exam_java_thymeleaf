package com.groupeisi.ExamGestionEtudiant.services;


import com.groupeisi.ExamGestionEtudiant.dao.FiliereDao;
import com.groupeisi.ExamGestionEtudiant.dto.FiliereDto;
import com.groupeisi.ExamGestionEtudiant.entities.FiliereEntity;
import com.groupeisi.ExamGestionEtudiant.exception.RequestException;
import com.groupeisi.ExamGestionEtudiant.mapper.FiliereMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@CacheConfig(cacheNames = "filieres")
public class FiliereService {

    private final FiliereDao filiereDao;
    private final FiliereMapper filiereMapper;
    private final MessageSource messageSource;

    public FiliereService(FiliereDao filiereDao, FiliereMapper filiereMapper, MessageSource messageSource) {
        this.filiereDao = filiereDao;
        this.filiereMapper = filiereMapper;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public List<FiliereDto> getAll() {
        return StreamSupport.stream(filiereDao.findAll().spliterator(), false)
                .map(filiereMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public FiliereDto getById(Long id) {
        FiliereEntity filiere = filiereDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("filiere.notfound", new Object[]{id}, Locale.getDefault())));
        return filiereMapper.toDto(filiere);
    }

    @Transactional
    public FiliereDto create(FiliereDto dto) {
        FiliereEntity entity = filiereMapper.toEntity(dto);
        FiliereEntity saved = filiereDao.save(entity);
        return filiereMapper.toDto(saved);
    }

    @CachePut(key = "#id")
    @Transactional
    public FiliereDto update(Long id, FiliereDto dto) {
        return filiereDao.findById(id)
                .map(existing -> {
                    existing.setCode(dto.getCode());
                    existing.setIntitule(dto.getIntitule());
                    FiliereEntity updated = filiereDao.save(existing);
                    return filiereMapper.toDto(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("filiere.notfound", new Object[]{id}, Locale.getDefault())));
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void delete(Long id) {
        try {
            filiereDao.deleteById(id);
        } catch (Exception e) {
            throw new RequestException(
                    messageSource.getMessage("filiere.errordeletion", new Object[]{id}, Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }
    }
}

