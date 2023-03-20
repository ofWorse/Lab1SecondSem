package jmxapp.enums;

public enum Status {

    RUNNING("запущен"),
    DONE("завершен"),
    CANCELLED("отменен"),
    ERROR("ошибка"),
    EMPTY("список пуст");

    private String taskStatus;

    Status(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    @Override
    public String toString() {
        return " ---> " + taskStatus + ".\n";
    }
}
