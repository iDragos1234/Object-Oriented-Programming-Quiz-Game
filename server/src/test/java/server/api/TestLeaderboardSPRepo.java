package server.api;

import commons.LeaderboardEntrySP;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.LeaderboardEntrySPRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestLeaderboardSPRepo implements LeaderboardEntrySPRepository {

    List<LeaderboardEntrySP> entries = new ArrayList<>();
    Stack<String> results = new Stack<String>();
    /**
     * Here a query is created with a specified criteria
     *
     * @return
     */
    @Override
    public List<LeaderboardEntrySP> findAllByOrderByScoreDescSP() {
        List<LeaderboardEntrySP> res = entries.stream().sorted(Comparator.comparing(LeaderboardEntrySP::getScore).reversed()).collect(Collectors.toList());
        if(res.size() < 10){
            return res;
        } else {
            return res.subList(0,10);
        }

    }

    //#################################################################################################################################

    @Override
    public List<LeaderboardEntrySP> findAll() {
        return entries;
    }

    //#################################################################################################################################

    @Override
    public List<LeaderboardEntrySP> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<LeaderboardEntrySP> findAllById(Iterable<Long> longs) {
        return null;
    }

    //#################################################################################################################################

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    @Override
    public Page<LeaderboardEntrySP> findAll(Pageable pageable) {
        return null;
    }

    //#################################################################################################################################

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    @Override
    public long count() {
        return entries.size();
    }

    //#################################################################################################################################

    /**
     * Deletes the entity with the given id.
     *
     * @param aLong must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    public void deleteById(Long aLong) {
        var user = entries.get(Math.toIntExact(aLong));
        int ind = entries.indexOf(user);
        entries.remove(ind);
        if(!entries.contains(user)){
            results.push("deleted");
        } else {
            results.push("not deleted or 2 same obj");
        }
    }

    //#################################################################################################################################

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public void delete(LeaderboardEntrySP entity) {
        entries.remove(entity);
        if(entries.contains(entity)){
            results.push("present");
        } else {
            results.push("deleted");
        }
    }

    //#################################################################################################################################

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     *
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(a -> entries.remove(entries.stream().filter(u -> u.getId()== a)));
    }

    //#################################################################################################################################

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    @Override
    public void deleteAll(Iterable<? extends LeaderboardEntrySP> entities) {
        entries.clear();
        if(entries.isEmpty()){
            results.add("empty");
        } else {
            results.add("deleteAll Failed");
        }
    }

    //#################################################################################################################################

    /**
     * Deletes all entities managed by the repository.
     */
    @Override
    public void deleteAll() {
        entries.clear();
        if(entries.isEmpty()){
            results.push("empty");
        } else {
            results.push("not empty");
        }
    }
//#################################################################################################################################
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Override
    public <S extends LeaderboardEntrySP> S save(S entity) {
        entries.add(entity);
        return entity;
    }
    //#################################################################################################################################
    @Override
    public <S extends LeaderboardEntrySP> List<S> saveAll(Iterable<S> entities) {
        List<S> res = new ArrayList<>();
        entities.forEach(res::add);
        entries.addAll(res);
        return res;
    }
    //#################################################################################################################################

    /**
     * Retrieves an entity by its id.
     *
     * @param aLong must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public Optional<LeaderboardEntrySP> findById(Long aLong) {
        var user = entries.stream().filter(u -> u.id==aLong).findFirst();
        if(user != null){
            results.push("found");
        } else {
            results.push("not found");
        }
        return user;
    }

    //#################################################################################################################################

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param aLong must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Override
    public void flush() {

    }

    /**
     * Saves an entity and flushes changes instantly.
     *
     * @param entity entity to be saved. Must not be {@literal null}.
     * @return the saved entity
     */
    @Override
    public <S extends LeaderboardEntrySP> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * Saves all entities and flushes changes instantly.
     *
     * @param entities entities to be saved. Must not be {@literal null}.
     * @return the saved entities
     * @since 2.5
     */
    @Override
    public <S extends LeaderboardEntrySP> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Deletes the given entities in a batch which means it will create a single query. This kind of operation leaves JPAs
     * first level cache and the database out of sync. Consider flushing the {@link ;EntityManager} before calling this
     * method.
     *
     * @param entities entities to be deleted. Must not be {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllInBatch(Iterable<LeaderboardEntrySP> entities) {

    }

    /**
     * Deletes the entities identified by the given ids using a single query. This kind of operation leaves JPAs first
     * level cache and the database out of sync. Consider flushing the {@link ;EntityManager} before calling this method.
     *
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
     * @since 2.5
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Deletes all entities in a batch call.
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
     * implemented this is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access. Some of them will reject invalid identifiers
     * immediately.
     *
     * @param aLong must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see ;Entity;Manager#getReference(Class, Object) for details on when an exception is thrown.
     * @deprecated use {@link ;Jpa;Repository#getById(ID)} instead.
     */
    @Override
    public LeaderboardEntrySP getOne(Long aLong) {
        return null;
    }

    //###############################################################################################################################

    /**
     * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
     * implemented this is very likely to always return an instance and throw an
     * {@link EntityNotFoundException} on first access. Some of them will reject invalid identifiers
     * immediately.
     *
     * @param aLong must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @see ;EntityManager#getReference(Class, Object) for details on when an exception is thrown.
     * @since 2.5
     */
    @Override
    public LeaderboardEntrySP getById(Long aLong) {
        return findById(aLong).get();
    }

    //###############################################################################################################################

    /**
     * Returns a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     *
     * @param example must not be {@literal null}.
     * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
     * @throws IncorrectResultSizeDataAccessException if the Example yields more than one result.
     */
    @Override
    public <S extends LeaderboardEntrySP> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends LeaderboardEntrySP> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntrySP> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
     * {@link Page} is returned.
     *
     * @param example  must not be {@literal null}.
     * @param pageable can be {@literal null}.
     * @return a {@link Page} of entities matching the given {@link Example}.
     */
    @Override
    public <S extends LeaderboardEntrySP> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Returns the number of instances matching the given {@link Example}.
     *
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @return the number of instances matching the {@link Example}.
     */
    @Override
    public <S extends LeaderboardEntrySP> long count(Example<S> example) {
        return 0;
    }

    /**
     * Checks whether the data store contains elements that match the given {@link Example}.
     *
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @return {@literal true} if the data store contains elements that match the given {@link Example}.
     */
    @Override
    public <S extends LeaderboardEntrySP> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Returns entities matching the given {@link Example} applying the {@link Function queryFunction} that defines the
     * query and its result type.
     *
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @return all entities matching the given {@link Example}.
     * @since 2.6
     */
    @Override
    public <S extends LeaderboardEntrySP, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
