package com.admin.catalogo.infrastructure;

import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }
}
