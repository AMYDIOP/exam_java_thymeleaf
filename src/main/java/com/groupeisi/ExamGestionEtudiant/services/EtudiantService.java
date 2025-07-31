package com.groupeisi.ExamGestionEtudiant.services;


import com.groupeisi.ExamGestionEtudiant.dao.EtudiantDao;
import com.groupeisi.ExamGestionEtudiant.dao.FiliereDao;
import com.groupeisi.ExamGestionEtudiant.dto.EtudiantDto;
import com.groupeisi.ExamGestionEtudiant.entities.EtudiantEntity;
import com.groupeisi.ExamGestionEtudiant.entities.FiliereEntity;
import com.groupeisi.ExamGestionEtudiant.exception.RequestException;
import com.groupeisi.ExamGestionEtudiant.mapper.EtudiantMapper;
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
@CacheConfig(cacheNames = "etudiants")
public class EtudiantService {

    private final EtudiantDao etudiantDao;
    private final EtudiantMapper etudiantMapper;
    private final FiliereDao filiereDao; // Pense à injecter aussi
    private final MessageSource messageSource;

    public EtudiantService(EtudiantDao etudiantDao, EtudiantMapper etudiantMapper, FiliereDao filiereDao, MessageSource messageSource) {
        this.etudiantDao = etudiantDao;
        this.etudiantMapper = etudiantMapper;
        this.filiereDao = filiereDao;
        this.messageSource = messageSource;
    }

    @Transactional(readOnly = true)
    public List<EtudiantDto> getAll() {
        return StreamSupport.stream(etudiantDao.findAll().spliterator(), false)
                .map(etudiantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public EtudiantDto getById(Long id) {
        EtudiantEntity etudiant = etudiantDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("etudiant.notfound", new Object[]{id}, Locale.getDefault())));
        return etudiantMapper.toDto(etudiant);
    }
    @Transactional
    public EtudiantDto create(EtudiantDto dto) {
        FiliereEntity filiere = filiereDao.findById(dto.getFiliereId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("filiere.notfound", new Object[]{dto.getFiliereId()}, Locale.getDefault())));

        EtudiantEntity entity = etudiantMapper.toEntity(dto);
        entity.setFiliere(filiere);
        EtudiantEntity saved = etudiantDao.save(entity);
        return etudiantMapper.toDto(saved);
    }


    @CachePut(key = "#id")
    @Transactional
    public EtudiantDto update(Long id, EtudiantDto dto) {
        return etudiantDao.findById(id)
                .map(existing -> {
                    FiliereEntity filiere = filiereDao.findById(dto.getFiliereId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    messageSource.getMessage("filiere.notfound", new Object[]{dto.getFiliereId()}, Locale.getDefault())));

                    existing.setNom(dto.getNom());
                    existing.setPrenom(dto.getPrenom());
                    existing.setEmail(dto.getEmail());
                    existing.setDateNaissance(dto.getDateNaissance());
                    existing.setClasse(dto.getClasse());
                    existing.setFiliere(filiere);

                    EtudiantEntity updated = etudiantDao.save(existing);
                    return etudiantMapper.toDto(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("etudiant.notfound", new Object[]{id}, Locale.getDefault())));
    }

    @CacheEvict(key = "#id")
    @Transactional
    public void delete(Long id) {
        try {
            etudiantDao.deleteById(id);
        } catch (Exception e) {
            throw new RequestException(
                    messageSource.getMessage("etudiant.errordeletion", new Object[]{id}, Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }
    }
}
