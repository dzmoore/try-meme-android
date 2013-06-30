package com.eastapps.mgs.web;

import com.eastapps.mgs.model.CrawlerBackground;
import com.eastapps.mgs.model.CrawlerMeme;
import com.eastapps.mgs.model.DeviceInfo;
import com.eastapps.mgs.model.Meme;
import com.eastapps.mgs.model.MemeBackground;
import com.eastapps.mgs.model.MemeText;
import com.eastapps.mgs.model.MemeUser;
import com.eastapps.mgs.model.SampleMeme;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Meme, String> getMemeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.Meme, java.lang.String>() {
            public String convert(Meme meme) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, Meme> getIdToMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.Meme>() {
            public com.eastapps.mgs.model.Meme convert(java.lang.Long id) {
                return Meme.findMeme(id);
            }
        };
    }

	public Converter<String, Meme> getStringToMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.Meme>() {
            public com.eastapps.mgs.model.Meme convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Meme.class);
            }
        };
    }

	public Converter<MemeBackground, String> getMemeBackgroundToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.MemeBackground, java.lang.String>() {
            public String convert(MemeBackground memeBackground) {
                return new StringBuilder().append(memeBackground.getFilePath()).append(' ').append(memeBackground.getDescription()).toString();
            }
        };
    }

	public Converter<Long, MemeBackground> getIdToMemeBackgroundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.MemeBackground>() {
            public com.eastapps.mgs.model.MemeBackground convert(java.lang.Long id) {
                return MemeBackground.findMemeBackground(id);
            }
        };
    }

	public Converter<String, MemeBackground> getStringToMemeBackgroundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.MemeBackground>() {
            public com.eastapps.mgs.model.MemeBackground convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MemeBackground.class);
            }
        };
    }

	public Converter<MemeText, String> getMemeTextToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.MemeText, java.lang.String>() {
            public String convert(MemeText memeText) {
                return new StringBuilder().append(memeText.getText()).append(' ').append(memeText.getFontSize()).toString();
            }
        };
    }

	public Converter<Long, MemeText> getIdToMemeTextConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.MemeText>() {
            public com.eastapps.mgs.model.MemeText convert(java.lang.Long id) {
                return MemeText.findMemeText(id);
            }
        };
    }

	public Converter<String, MemeText> getStringToMemeTextConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.MemeText>() {
            public com.eastapps.mgs.model.MemeText convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MemeText.class);
            }
        };
    }

	public Converter<MemeUser, String> getMemeUserToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.MemeUser, java.lang.String>() {
            public String convert(MemeUser memeUser) {
                return new StringBuilder().append(memeUser.getUsername()).append(' ').append(memeUser.getPassword()).append(' ').append(memeUser.getSalt()).toString();
            }
        };
    }

	public Converter<Long, MemeUser> getIdToMemeUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.MemeUser>() {
            public com.eastapps.mgs.model.MemeUser convert(java.lang.Long id) {
                return MemeUser.findMemeUser(id);
            }
        };
    }

	public Converter<String, MemeUser> getStringToMemeUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.MemeUser>() {
            public com.eastapps.mgs.model.MemeUser convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MemeUser.class);
            }
        };
    }

	public Converter<SampleMeme, String> getSampleMemeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.SampleMeme, java.lang.String>() {
            public String convert(SampleMeme sampleMeme) {
                return "(no displayable fields)";
            }
        };
    }

	public Converter<Long, SampleMeme> getIdToSampleMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.SampleMeme>() {
            public com.eastapps.mgs.model.SampleMeme convert(java.lang.Long id) {
                return SampleMeme.findSampleMeme(id);
            }
        };
    }

	public Converter<String, SampleMeme> getStringToSampleMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.SampleMeme>() {
            public com.eastapps.mgs.model.SampleMeme convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SampleMeme.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getMemeToStringConverter());
        registry.addConverter(getIdToMemeConverter());
        registry.addConverter(getStringToMemeConverter());
        registry.addConverter(getMemeBackgroundToStringConverter());
        registry.addConverter(getIdToMemeBackgroundConverter());
        registry.addConverter(getStringToMemeBackgroundConverter());
        registry.addConverter(getMemeTextToStringConverter());
        registry.addConverter(getIdToMemeTextConverter());
        registry.addConverter(getStringToMemeTextConverter());
        registry.addConverter(getMemeUserToStringConverter());
        registry.addConverter(getIdToMemeUserConverter());
        registry.addConverter(getStringToMemeUserConverter());
        registry.addConverter(getSampleMemeToStringConverter());
        registry.addConverter(getIdToSampleMemeConverter());
        registry.addConverter(getStringToSampleMemeConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

	public Converter<CrawlerBackground, String> getCrawlerBackgroundToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.CrawlerBackground, java.lang.String>() {
            public String convert(CrawlerBackground crawlerBackground) {
                return new StringBuilder().append(crawlerBackground.getSourceDesc()).append(' ').append(crawlerBackground.getCrawlerImgFilename()).append(' ').append(crawlerBackground.getCrawlerImgDesc()).toString();
            }
        };
    }

	public Converter<Long, CrawlerBackground> getIdToCrawlerBackgroundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.CrawlerBackground>() {
            public com.eastapps.mgs.model.CrawlerBackground convert(java.lang.Long id) {
                return CrawlerBackground.findCrawlerBackground(id);
            }
        };
    }

	public Converter<String, CrawlerBackground> getStringToCrawlerBackgroundConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.CrawlerBackground>() {
            public com.eastapps.mgs.model.CrawlerBackground convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CrawlerBackground.class);
            }
        };
    }

	public Converter<CrawlerMeme, String> getCrawlerMemeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.CrawlerMeme, java.lang.String>() {
            public String convert(CrawlerMeme crawlerMeme) {
                return new StringBuilder().append(crawlerMeme.getName()).append(' ').append(crawlerMeme.getTopText()).append(' ').append(crawlerMeme.getBottomText()).toString();
            }
        };
    }

	public Converter<Long, CrawlerMeme> getIdToCrawlerMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.CrawlerMeme>() {
            public com.eastapps.mgs.model.CrawlerMeme convert(java.lang.Long id) {
                return CrawlerMeme.findCrawlerMeme(id);
            }
        };
    }

	public Converter<String, CrawlerMeme> getStringToCrawlerMemeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.CrawlerMeme>() {
            public com.eastapps.mgs.model.CrawlerMeme convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CrawlerMeme.class);
            }
        };
    }

	public Converter<DeviceInfo, String> getDeviceInfoToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.eastapps.mgs.model.DeviceInfo, java.lang.String>() {
            public String convert(DeviceInfo deviceInfo) {
                return new StringBuilder().append(deviceInfo.getUniqueId()).toString();
            }
        };
    }

	public Converter<Long, DeviceInfo> getIdToDeviceInfoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.eastapps.mgs.model.DeviceInfo>() {
            public com.eastapps.mgs.model.DeviceInfo convert(java.lang.Long id) {
                return DeviceInfo.findDeviceInfo(id);
            }
        };
    }

	public Converter<String, DeviceInfo> getStringToDeviceInfoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.eastapps.mgs.model.DeviceInfo>() {
            public com.eastapps.mgs.model.DeviceInfo convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DeviceInfo.class);
            }
        };
    }
}
