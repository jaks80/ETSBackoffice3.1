package com.ets.otherservice.service;

import com.ets.otherservice.dao.AdditionalChargeDAO;
import com.ets.otherservice.domain.AdditionalCharge;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("additionalChargeService")
public class AdditionalChargeService {

    @Resource(name = "additionalChargeDAO")
    private AdditionalChargeDAO dao;

    public List<AdditionalCharge> findAll() {
        return dao.findAll(AdditionalCharge.class);
    }

    public AdditionalCharge saveorUpdate(AdditionalCharge additionalCharge) {
        dao.save(additionalCharge);
        return additionalCharge;
    }

    public void delete(AdditionalCharge additionalCharge) {
        dao.delete(additionalCharge);
    }
}
