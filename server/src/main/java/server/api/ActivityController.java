package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import commons.Activity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    // TODO: fix the POST method, since it does not do what it is supposed to do

    private Random random;
    private ActivityRepository repo;

    public ActivityController(Random random, ActivityRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> saved = repo.findAll();
        return ResponseEntity.ok(saved);
    }

    /**
     * now throws exception if id does not exist
     *
     * @param id
     * @return
     */
    @GetMapping("/get_id/{id}")
    public ResponseEntity<Activity> getById(@PathVariable String id) {
        if (repo.findById(id).isPresent()) {
            Activity saved = repo.getById(id);
            return ResponseEntity.ok(saved);
        } else {
            throw new EntityNotFoundException();
        }

    }

    @GetMapping("/rnd")
    public ResponseEntity<Activity> getRandom() {
        var idx = random.nextInt((int) repo.count());

        Optional<Activity> activity = repo.getByIndex((long) idx);

        if (!activity.isPresent()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(activity.get());
    }

    @GetMapping("/rnd/list")
    public ResponseEntity<List<Activity>> getRandomListOfActivities(int numberOfActivities) {
        List<Activity> list = new LinkedList<>();
        int repoBound = (int) repo.count();
        for (int i = 0; i < numberOfActivities; i++) {
            var idx = random.nextInt(repoBound);

            Optional<Activity> activity = repo.getByIndex((long) idx);

            if (activity.isPresent()) {
                list.add(activity.get());
            } else return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/post")
    public ResponseEntity<Activity> postActivity(@Validated @RequestBody Activity activity) {
        if (activity.id == null || activity.id.isEmpty()
                || isNullOrEmpty(activity.title)
                || isNullOrEmpty(activity.image_path)
                || isNullOrEmpty(activity.source)) {
            return ResponseEntity.badRequest().build();
        }
        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity newActivity, @PathVariable String id) {

        if (newActivity.id == null || newActivity.id.isEmpty()
                || isNullOrEmpty(newActivity.title)
                || isNullOrEmpty(newActivity.image_path)
                || isNullOrEmpty(newActivity.source)) {
            return ResponseEntity.badRequest().build();
        }


        return repo.findById(id)
                .map(activity -> {
                    activity.setTitle(newActivity.getTitle());
                    activity.setConsumption_in_wh(newActivity.getConsumption_in_wh());
                    activity.setImage_path(newActivity.getImage_path());
                    activity.setSource(newActivity.getSource());
                    activity.setIndex(newActivity.getIndex());
                    Activity saved = repo.save(activity);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    newActivity.setId(id);
                    Activity saved = repo.save(newActivity);
                    return ResponseEntity.ok(saved);
                });
    }

    @DeleteMapping("/delete/{id}")
    public void deleteActivity(@PathVariable String id) {
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
        }

    }


    /**
     * Deletes the last item in the repository， might be used for the admin
     */
    @DeleteMapping("delete_last")
    public void deleteLastAcivity() {
        List<Activity> activities = repo.findAll();
        int lastIndex = activities.size() - 1;
        String lastId = activities.get(lastIndex).getId();
        if (repo.findById(lastId).isPresent()) {
            repo.deleteById(lastId);
        }
    }

    @DeleteMapping("/delete_all")
    public void deleteAllActivities() {
        repo.deleteAll();
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Use this method after updating the activities.json file. It will make sure the activity repository gets updated.
     */
    @GetMapping("/updateJsonFile")
    public void setUpdatedActivities() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Activity[] activityList = mapper.readValue(new File("server/src/main/resources/activities.json"), Activity[].class);

            for (int i = 0; i < activityList.length; i++) {
                updateActivitiesFromJson(activityList[i], i);
            }
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This activities updates an activity in the repository, if it does not exist yet it creates a new entry.
     * The updateActivity method above does not work because of the spring annotation, so i copied it here without annotations!
     *
     * @param newActivity to be added to the repository
     */
    public void updateActivitiesFromJson(Activity newActivity, int i) {

        if (newActivity.id == null || newActivity.id.isEmpty()
                || isNullOrEmpty(newActivity.title)
                || isNullOrEmpty(newActivity.image_path)
                || isNullOrEmpty(newActivity.source)) {
            return;
        }

        repo.findById(newActivity.getId())
                .map(activity -> {
                    activity.setTitle(newActivity.getTitle());
                    activity.setConsumption_in_wh(newActivity.getConsumption_in_wh());
                    activity.setImage_path(newActivity.getImage_path());
                    activity.setSource(newActivity.getSource());
                    activity.setIndex((long) i);
                    repo.save(activity);
                    return null;
                })
                .orElseGet(() -> {
                    newActivity.setId(newActivity.getId());
                    newActivity.setIndex((long) i);
                    repo.save(newActivity);

                    return null;
                });
    }

    /**
     * used for admin scene
     * used after delete method
     * so all the questions after the question that got deleted will get updated
     * their index will get -1
     * however this does not work and I could not figure it out
     * @param index of the question got deleted
     */
    //  @GetMapping("/updateAfter/{index}")
    // public void updateAfter(@PathVariable long index){
    //   List<Activity> activities = repo.findAll();
    //    for(int i=(int)index+1;index<activities.size();i++){
    //       activities.get(i).setIndex(activities.get(i).getIndex()-1);
    //    repo.save(activities.get(i));
    //doesn't work
    //    }
//    }


    // Could not figure it out.
    //  @GetMapping("/changeActivity")
    //  public void setChangedActivity(){
    //   }
    // public void changeActivityById(String id, String newId, String newTitle, String newSource, String newImagePath, long newConsumption) {
    //  Activity activity = repo.getById(id);
    // activity.setId(newId);
    //  activity.setTitle(newTitle);
    // activity.setSource(newSource);
    //  activity.setImage_path(newImagePath);
    //  activity.setConsumption_in_wh(newConsumption);
    //}
}
