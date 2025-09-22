package com.espeditomelo.myblog.model.repository;

import com.espeditomelo.myblog.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
