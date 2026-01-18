package org.example.catalogservice.service;

import org.example.catalogservice.controller.CatalogController;
import org.example.catalogservice.jpa.CatalogEntity;
import org.example.catalogservice.jpa.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService{
    CatalogRepository catalogRepository;
    Environment env;

    @Autowired
    public CatalogServiceImpl(CatalogRepository catalogRepository, Environment env) {
        this.catalogRepository = catalogRepository;
        this.env = env;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
