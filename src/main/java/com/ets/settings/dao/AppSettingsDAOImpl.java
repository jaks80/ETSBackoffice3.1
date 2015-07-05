package com.ets.settings.dao;

import com.ets.GenericDAOImpl;
import com.ets.settings.domain.AppSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("appSettingsDAO")
@Transactional
public class AppSettingsDAOImpl  extends GenericDAOImpl<AppSettings, Long> implements AppSettingsDAO{
    
}
