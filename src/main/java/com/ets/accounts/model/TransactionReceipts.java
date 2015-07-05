package com.ets.accounts.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class TransactionReceipts {

    @XmlElement
    private List<TransactionReceipt> list = new ArrayList<>();

    public List<TransactionReceipt> getList() {
        return list;
    }

    public void setList(List<TransactionReceipt> list) {
        this.list = list;
    }
}
