package ar.edu.ubp.das.mascotasrest.services;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import ar.edu.ubp.das.mascotasrest.repositories.EspeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspeciesService {

    @Autowired
    private EspeciesRepository repository;

    public List<EspecieBE> getEspecies() {
        return repository.getEspecies();
    }
}
