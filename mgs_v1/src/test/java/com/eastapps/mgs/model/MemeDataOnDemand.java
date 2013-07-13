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
@RooDataOnDemand(entity = Meme.class)
public class MemeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Meme> data;

	public Meme getNewTransientMeme(int index) {
        Meme obj = new Meme();
        return obj;
    }

	public Meme getSpecificMeme(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Meme obj = data.get(index);
        Long id = obj.getId();
        return Meme.findMeme(id);
    }

	public Meme getRandomMeme() {
        init();
        Meme obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Meme.findMeme(id);
    }

	public boolean modifyMeme(Meme obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Meme.findMemeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Meme' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Meme>();
        for (int i = 0; i < 10; i++) {
            Meme obj = getNewTransientMeme(i);
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

	public void setBottomText(Meme obj, int index) {
        MemeText bottomText = null;
        obj.setBottomText(bottomText);
    }

	public void setCreatedByUser(Meme obj, int index) {
        MemeUser createdByUser = null;
        obj.setCreatedByUser(createdByUser);
    }

	public void setMemeBackground(Meme obj, int index) {
        MemeBackground memeBackground = null;
        obj.setMemeBackground(memeBackground);
    }

	public void setTopText(Meme obj, int index) {
        MemeText topText = null;
        obj.setTopText(topText);
    }
}
