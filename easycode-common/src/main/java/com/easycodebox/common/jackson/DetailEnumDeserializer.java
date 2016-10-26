package com.easycodebox.common.jackson;

import java.io.IOException;

import com.easycodebox.common.enums.DetailEnum;
import com.easycodebox.common.enums.DetailEnums;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

/**
 * DetailEnum的String值可以是：
 * 1、{"value":"0"} ==> 对象形式
 * 2、YES ==> enum class name
 * 3、0	==> DetailEnum的value属性
 * 4、是	==> DetailEnum的desc属性
 * 5、枚举的索引值
 * 转换优先级顺序: className -> value属性 -> desc属性 -> 枚举的索引值
 * @author WangXiaoJin
 *
 */
public class DetailEnumDeserializer extends JsonDeserializer<DetailEnum<?>> implements ContextualDeserializer {
	
	private Class<DetailEnum<?>> enumClass;
	
	public DetailEnumDeserializer() {
		super();
	}
	
	public DetailEnumDeserializer(Class<DetailEnum<?>> enumClass) {
		super();
		this.enumClass = enumClass;
	}

	@Override
	public DetailEnum<?> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		JsonNode node = jp.getCodec().readTree(jp);
		String value = null;
		if(node.isObject()) {
			JsonNode jn = node.get("value");
			value = jn != null ? jn.asText() : value;
		}else {
			value = node.asText();
		}
		return (DetailEnum<?>)DetailEnums.deserialize(enumClass, value, !ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS));
    }

	/**
	 * 根据DetailEnum实际运行类型返回JsonDeserializer
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
			BeanProperty property) throws JsonMappingException {
		return new DetailEnumDeserializer((Class<DetailEnum<?>>)ctxt.getContextualType().getRawClass());
	}



}
