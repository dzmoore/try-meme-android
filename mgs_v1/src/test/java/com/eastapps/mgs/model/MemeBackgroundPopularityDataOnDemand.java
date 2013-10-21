package com.eastapps.mgs.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = MemeBackgroundPopularity.class)
public class MemeBackgroundPopularityDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<MemeBackgroundPopularity> data;

	@Autowired
    LvPopularityTypeDataOnDemand lvPopularityTypeDataOnDemand;

	public MemeBackgroundPopularity getNewTransientMemeBackgroundPopularity(int index) {
        MemeBackgroundPopularity obj = new MemeBackgroundPopularity();
        setMemeBackgroundPopularityValue(obj, index);
        return obj;
    }

	public void setMemeBackgroundPopularityValue(MemeBackgroundPopularity obj, int index) {
        Long memeBackgroundPopularityValue = new Integer(index).longValue();
        obj.setMemeBackgroundPopularityValue(memeBackgroundPopularityValue);
    }

	public MemeBackgroundPopularity getSpecificMemeBackgroundPopularity(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MemeBackgroundPopularity obj = data.get(index);
        Long id = obj.getId();
        return MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
    }

	public MemeBackgroundPopularity getRandomMemeBackgroundPopularity() {
        init();
        MemeBackgroundPopularity obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MemeBackgroundPopularity.findMemeBackgroundPopularity(id);
    }

	public boolean modifyMemeBackgroundPopularity(MemeBackgroundPopularity obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = MemeBackgroundPopularity.findMemeBackgroundPopularityEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MemeBackgroundPopularity' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MemeBackgroundPopularity>();
        for (int i = 0; i < 10; i++) {
            MemeBackgroundPopularity obj = getNewTransientMemeBackgroundPopularity(i);
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
