package com.ets.usertask.domain;

import com.ets.PersistentObject;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf Akhond
 */
//@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "user_task")
public class UserTask extends PersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private Date remindDate;
    @XmlElement
    private Integer remindTime;
    @XmlElement
    private String title;
    @XmlElement
    private String description;
    @XmlElement
    private User taskFor;
    @XmlElement
    private Enums.TaskPriority taskPriority;

    public UserTask() {

    }

    @OneToOne
    @JoinColumn(name = "taskfor_fk")
    public User getTaskFor() {
        return taskFor;
    }

    public void setTaskFor(User taskFor) {
        this.taskFor = taskFor;
    }

    public Enums.TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Enums.TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public Integer getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Integer remindTime) {
        this.remindTime = remindTime;
    }
}
