package ua.ithillel.javapro;

import ua.ithillel.javapro.reflection.Id;
import ua.ithillel.javapro.reflection.Table;

@Table(name = "lesson")
public class LessonEntity {

    @Id
    private Integer id;
    private String name;
    private HomeworkEntity homework;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HomeworkEntity getHomework() {
        return homework;
    }

    public void setHomework(HomeworkEntity homework) {
        this.homework = homework;
    }

}
