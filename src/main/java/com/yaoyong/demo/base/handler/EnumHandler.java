package com.yaoyong.demo.base.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import com.baomidou.springboot.entity.enums.AgeEnum;




@Component
@MappedJdbcTypes({ JdbcType.INTEGER })
@MappedTypes({ AgeEnum.class })
public final class EnumHandler<E extends BaseEnum<?, ?>> extends BaseTypeHandler<E> {

	private Class<E> type;
	private E[] enums;

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		System.out.println("--------------------------------------------------数据库处理ENUM");
		// BaseTypeHandler已经帮我们做了parameter的null判断
		ps.setObject(i, parameter.getValue(), jdbcType.TYPE_CODE);
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放String类型
		String i = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的value值，定位PersonType子类
			return locateEnumStatus(i);

		}

	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放String类型
		String i = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的value值，定位PersonType子类
			return locateEnumStatus(i);
		}
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，本例子中数据库中存放String类型
		String i = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			// 根据数据库中的value值，定位PersonType子类
			return locateEnumStatus(i);
		}

	}

	/**
	 *
	 * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
	 *
	 * @param value
	 *            数据库中存储的自定义value属性
	 *
	 * @return value对应的枚举类
	 *
	 */

	private E locateEnumStatus(String value) {
		System.out.println("--------------------------------------------------数据库处理ENUM");

		for (E e : enums) {
			if (e.getValue().equals(value)) {
				return e;
			}
		}
		throw new IllegalArgumentException("未知的枚举类型：" + value + ",请核对" + type.getSimpleName());

	}

}
