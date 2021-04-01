package com.hpl.blog.dao;

import com.hpl.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogsRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog>{

    @Query("select b from Blog b where recommendState = true and publishState = true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findBySearchText(String searchText,Pageable pageable);

    @Query("select b from Blog b where type.id = ?1")
    Page<Blog> findByTypeId(Long typeId,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.viewNum = b.viewNum+1 where b.id = ?1")
    int updateViewNum(Long blogId);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc")
    List<String> findGroupbyYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 order by updateTime desc")
    List<Blog> findByYear(String year);

    @Query("select sum (viewNum) as totalViewNum from Blog")
    Long getSumViewNum();
}
