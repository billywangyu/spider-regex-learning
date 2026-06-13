package com.learning.repository;
import com.learning.entity.UserWordProgress;
import com.learning.entity.UserWordProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UserWordProgressRepository extends JpaRepository<UserWordProgress, UserWordProgressId> {
    List<UserWordProgress> findByUserId(String userId);
}
