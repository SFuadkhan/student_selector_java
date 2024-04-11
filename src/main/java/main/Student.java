package main;

public class Student {
    private final Integer id;
    private final String name;
    private Integer answers;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAnswers() {
        return answers;
    }

    public void setAnswers(Integer answers) {
        this.answers = answers;
    }

    public Student(Integer id, String name, Integer answers) {
        this.id = id;
        this.name = name;
        this.answers = answers;
    }
}
