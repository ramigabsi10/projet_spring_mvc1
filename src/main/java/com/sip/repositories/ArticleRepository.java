package com.sip.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sip.entities.Article;

public interface ArticleRepository  extends CrudRepository<Article, Long> {

}
