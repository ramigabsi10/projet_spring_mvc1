package com.sip.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sip.entities.News;

public interface NewsRepository  extends CrudRepository<News, Long> {

}
