package com.eastapps.mgs.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = LvPopularityType.class)
public class LvPopularityTypeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<LvPopularityType> data;

	public LvPopularityType getNewTransientLvPopularityType(int index) {
        LvPopularityType obj = new LvPopularityType();
        setPopularityTypeName(obj, index);
        return obj;
    }

	public void setPopularityTypeName(LvPopularityType obj, int index) {
        String popularityTypeName = "popularityTypeName_" + index;
        obj.setPopularityTypeName(popularityTypeName);
    }

	public LvPopularityType getSpecificLvPopularityType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        LvPopularityType obj = data.get(index);
        Long id = obj.getId();
        return LvPopularityType.findLvPopularityType(id);
    }

	public LvPopularityType getRandomLvPopularityType() {
        init();
        LvPopularityType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return LvPopularityType.findLvPopularityType(id);
    }

	public boolean modifyLvPopularityType(LvPopularityType obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = LvPopularityType.findLvPopularityTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'LvPopularityType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<LvPopularityType>();
        for (int i = 0; i < 10; i++) {
            LvPopularityType obj = getNewTransientLvPopularityType(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
