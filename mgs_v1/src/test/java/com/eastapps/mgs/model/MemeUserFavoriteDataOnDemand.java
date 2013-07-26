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
@RooDataOnDemand(entity = MemeUserFavorite.class)
public class MemeUserFavoriteDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<MemeUserFavorite> data;

	public MemeUserFavorite getNewTransientMemeUserFavorite(int index) {
        MemeUserFavorite obj = new MemeUserFavorite();
        setMemeBackground(obj, index);
        setMemeUser(obj, index);
        return obj;
    }

	public void setMemeBackground(MemeUserFavorite obj, int index) {
        MemeBackground memeBackground = null;
        obj.setMemeBackground(memeBackground);
    }

	public void setMemeUser(MemeUserFavorite obj, int index) {
        MemeUser memeUser = null;
        obj.setMemeUser(memeUser);
    }

	public MemeUserFavorite getSpecificMemeUserFavorite(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MemeUserFavorite obj = data.get(index);
        Long id = obj.getId();
        return MemeUserFavorite.findMemeUserFavorite(id);
    }

	public MemeUserFavorite getRandomMemeUserFavorite() {
        init();
        MemeUserFavorite obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MemeUserFavorite.findMemeUserFavorite(id);
    }

	public boolean modifyMemeUserFavorite(MemeUserFavorite obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = MemeUserFavorite.findMemeUserFavoriteEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MemeUserFavorite' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MemeUserFavorite>();
        for (int i = 0; i < 10; i++) {
            MemeUserFavorite obj = getNewTransientMemeUserFavorite(i);
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
