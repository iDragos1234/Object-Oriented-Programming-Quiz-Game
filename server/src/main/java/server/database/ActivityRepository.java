package server.database;

import commons.Activity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    public default Optional<Activity> getByIndex(long idx) {
        return this.findAll().stream().filter(activity -> activity.index == idx).findFirst();
    }
}
