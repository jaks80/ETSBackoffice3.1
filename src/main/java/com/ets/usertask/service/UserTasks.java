package com.ets.usertask.service;

import com.ets.usertask.domain.UserTask;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf Akhond
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class UserTasks {

    @XmlElement
    private List<UserTask> list = new ArrayList<>();

    public List<UserTask> getList() {
        return list;
    }

    public void setList(List<UserTask> list) {
        this.list = list;
    }
}
