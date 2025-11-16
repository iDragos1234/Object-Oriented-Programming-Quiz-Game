package server.api;

import commons.Activity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ActivityRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestActivityRepository implements ActivityRepository{

    public final List<Activity> activities = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Activity> findAll() {
        call("findAll");
        return activities;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return null;
    }


    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<String> strings) {
        return null;
    }


    @Override
    public long count() {
        call("count");
        return activities.size();
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param s must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    public void deleteById(String s) {
        call("deleteById");
        activities.stream().filter(a -> a.id.equals(s)).findFirst().ifPresent(activities::remove);
    }

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public void delete(Activity entity) {
        call("delete");
        activities.removeAll(activities);
    }

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     *
     * @param strings must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {

    }

    /**
     * Deletes all entities managed by the repository.
     */
    @Override
    public void deleteAll() {
        call("deleteAll");
        activities.removeAll(activities);
    }


    @Override
    public <S extends Activity> S save(S entity) {
        call("save");
//        entity.id = Integer.toString((int) activities.size());
        activities.add(entity);
        return entity;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        call("saveAll");
        Iterator<S> iter = entities.iterator();
        while(iter.hasNext()) {
            S elem = iter.next();
            if(!activities.contains(elem)) activities.add(elem);
        }
        return (List<S>) activities;
    }


    @Override
    public Optional<Activity> findById(String s) {
        call("findById");
        return activities.stream().filter(activity -> activity.id.equals(s)).findFirst();
    }


    @Override
    public boolean existsById(String s) {
        call("existsById");
        return find(s).isPresent();
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Override
    public void flush() {

    }


    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        return null;
    }


    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }


    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {

    }


    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }
    
    @Override
    public void deleteAllInBatch() {

    }


    @Override
    public Activity getOne(String s) {
        return null;
    }


    @Override
    public Activity getById(String s) {
        call("getById");
        System.out.println(activities);
        return find(s).get();
    }

    private Optional<Activity> find(String id) {
        return activities.stream().filter(q -> q.id.equals(id)).findFirst();
    }


    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }


    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }


    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }


    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }


    @Override
    public <S extends Activity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
    
    @Override
    public Optional<Activity> getByIndex(long idx) throws IllegalArgumentException{
        call("getByIndex");
        
        if(idx < 0 && idx >= activities.size()) {
            throw new IllegalArgumentException();
        }
        
        return Optional.ofNullable(activities.get((int) idx));
    }

}
