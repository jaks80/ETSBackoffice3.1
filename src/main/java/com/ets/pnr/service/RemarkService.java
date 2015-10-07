package com.ets.pnr.service;

import com.ets.pnr.dao.RemarkDAO;
import com.ets.pnr.domain.Remark;
import com.ets.pnr.model.RemarkSummary;
import com.ets.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("remarkService")
public class RemarkService {

    @Resource(name = "remarkDAO")
    private RemarkDAO dao;

    public List<RemarkSummary> getByPnrId(Long pnrId) {
        List<Remark> list = dao.getByPnrId(pnrId);
        List<RemarkSummary> summery_list = new ArrayList<>();

        for (Remark r : list) {
            RemarkSummary s = new RemarkSummary();
            s.setId(r.getId());
            s.setText(r.getText());
            s.setCreatedBy(r.getCreatedBy().calculateFullName());
            if(r.getDateTime()!=null){
             s.setDateTime(DateUtil.dateToString(r.getDateTime(), "ddMMMyyyy HH:mm:ss"));
            }else{
             s.setDateTime("");
            }

            summery_list.add(s);
        }
        return summery_list;
    }

    public void save(Remark remark) {
        dao.save(remark);
    }

    public void saveBulk(List<Remark> remarks) {
        dao.saveBulk(remarks);
    }
}
