package com.ets.pnr.service;

import com.ets.pnr.dao.AirlineDAO;
import com.ets.pnr.dao.RemarkDAO;
import com.ets.pnr.domain.Airline;
import com.ets.pnr.domain.Remark;
import com.ets.pnr.model.collection.Airlines;
import com.ets.pnr.logic.PnrUtil;
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
    
        public List<Remark> getByPnrId(Long pnrId) {
        List<Remark> list = dao.getByPnrId(pnrId);
        for (Remark a : list) {
            PnrUtil.undefineChildrenInPnr(a.getPnr());            
        }
        return list;
    }
    
    public void save(Remark remark){
     dao.save(remark);
    }
    
    public void saveBulk(List<Remark> remarks){
     dao.saveBulk(remarks);
    }
}
