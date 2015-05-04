package org.jetbrains.jdba.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jdba.core.exceptions.DBPreparingException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;



/**
 * Value getters factory.
 * <p/>
 * Stateless service.
 *
 * @author Leonid Bushuev from JetBrains
 */
public final class JdbcValueGetters {



  //// INTERNAL STRUCTURES \\\\

  protected static final class SpecificKey {
    final int jdbcType;
    final @NotNull Class<?> desiredClass;

    SpecificKey(final int jdbcType, @NotNull final Class<?> desiredClass) {
      this.jdbcType = jdbcType;
      this.desiredClass = desiredClass;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SpecificKey that = (SpecificKey) o;

      return this.jdbcType == that.jdbcType && this.desiredClass == that.desiredClass;
    }

    @Override
    public int hashCode() {
      return Math.abs(jdbcType)*17 + desiredClass.hashCode();
    }
  }


  //// INSTANCE PLENTY \\\\

  @NotNull
  private static final Map<Class<?>, JdbcValueGetter<?>> NORMAL_GETTERS;

  @NotNull
  private static final Map<SpecificKey, JdbcValueGetter<?>> SPECIFIC_GETTERS;


  static {
    NORMAL_GETTERS = new HashMap<Class<?>, JdbcValueGetter<?>>(30);
    NORMAL_GETTERS.put(boolean.class, IntBoolGetter.INSTANCE);
    NORMAL_GETTERS.put(Boolean.class, IntBoolGetter.INSTANCE);
    NORMAL_GETTERS.put(byte.class, ByteGetter.INSTANCE);
    NORMAL_GETTERS.put(Byte.class, ByteGetter.INSTANCE);
    NORMAL_GETTERS.put(short.class, ShortGetter.INSTANCE);
    NORMAL_GETTERS.put(Short.class, ShortGetter.INSTANCE);
    NORMAL_GETTERS.put(int.class, IntGetter.INSTANCE);
    NORMAL_GETTERS.put(Integer.class, IntGetter.INSTANCE);
    NORMAL_GETTERS.put(long.class, LongGetter.INSTANCE);
    NORMAL_GETTERS.put(Long.class, LongGetter.INSTANCE);
    NORMAL_GETTERS.put(float.class, FloatGetter.INSTANCE);
    NORMAL_GETTERS.put(Float.class, FloatGetter.INSTANCE);
    NORMAL_GETTERS.put(double.class, DoubleGetter.INSTANCE);
    NORMAL_GETTERS.put(Double.class, DoubleGetter.INSTANCE);
    NORMAL_GETTERS.put(BigDecimal.class, BigDecimalGetter.INSTANCE);
    NORMAL_GETTERS.put(String.class, StringGetter.INSTANCE);
    NORMAL_GETTERS.put(char.class, CharGetter.INSTANCE);
    NORMAL_GETTERS.put(Character.class, CharGetter.INSTANCE);
    NORMAL_GETTERS.put(java.util.Date.class, JavaDateGetter.INSTANCE);
    NORMAL_GETTERS.put(java.sql.Date.class, DateGetter.INSTANCE);
    NORMAL_GETTERS.put(Timestamp.class, TimestampGetter.INSTANCE);
    NORMAL_GETTERS.put(java.sql.Time.class, TimeGetter.INSTANCE);
    NORMAL_GETTERS.put(Object.class, ObjectGetter.INSTANCE);

    SPECIFIC_GETTERS = new HashMap<SpecificKey, JdbcValueGetter<?>>(30);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, boolean.class), BoolBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, Boolean.class), BoolBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, byte.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, Byte.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, short.class), BoolShortGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, Short.class), BoolShortGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, int.class), BoolIntGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, Integer.class), BoolIntGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BOOLEAN, Number.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, boolean.class), BoolBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, Boolean.class), BoolBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, byte.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, Byte.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, short.class), BoolShortGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, Short.class), BoolShortGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, int.class), BoolIntGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, Integer.class), BoolIntGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIT, Number.class), BoolByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.TINYINT, boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.TINYINT, Boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.TINYINT, Number.class), ByteGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.SMALLINT, boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.SMALLINT, Boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.SMALLINT, Number.class), ShortGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.INTEGER, boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.INTEGER, Boolean.class), IntBoolGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.INTEGER, Number.class), IntGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.BIGINT, Number.class), LongGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.FLOAT, Number.class), FloatGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.DOUBLE, Number.class), DoubleGetter.INSTANCE);
    SPECIFIC_GETTERS.put(new SpecificKey(Types.DECIMAL, Number.class), BigDecimalGetter.INSTANCE);
  }



  @NotNull
  @SuppressWarnings("unchecked")
  static <W> JdbcValueGetter<W> of(final int jdbcType, @NotNull final Class<W> clazz) {
    JdbcValueGetter<W> getter = find(jdbcType, clazz);
    if (getter == null) throw new DBPreparingException("Unknown how to get a value of class "+clazz.getSimpleName());
    return getter;
  }


  @Nullable
  @SuppressWarnings("unchecked")
  static <W> JdbcValueGetter<W> find(final int jdbcType, @NotNull final Class<W> clazz) {
    JdbcValueGetter<?> getter = null;
    if (jdbcType != Types.OTHER) getter = SPECIFIC_GETTERS.get(new SpecificKey(jdbcType, clazz));
    if (getter == null) getter = NORMAL_GETTERS.get(clazz);
    return (JdbcValueGetter<W>) getter;
  }



  //// GETTERS \\\\


  static final class BoolBoolGetter extends JdbcValueGetter<Boolean> {
    @Override
    @Nullable
    Boolean getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final boolean value = rset.getBoolean(index);
      return rset.wasNull() ? null : value;
    }


    static final BoolBoolGetter INSTANCE = new BoolBoolGetter();
  }

  @SuppressWarnings("UnnecessaryBoxing")
  static final class BoolByteGetter extends JdbcValueGetter<Byte> {
    @Override
    @Nullable
    Byte getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final boolean value = rset.getBoolean(index);
      return rset.wasNull() ? null : value ? _1_ : _0_;
    }

    private final static Byte _1_ = Byte.valueOf((byte)1);
    private final static Byte _0_ = Byte.valueOf((byte)0);

    final static BoolByteGetter INSTANCE = new BoolByteGetter();
  }

  @SuppressWarnings("UnnecessaryBoxing")
  static final class BoolShortGetter extends JdbcValueGetter<Short> {
    @Override
    @Nullable
    Short getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final boolean value = rset.getBoolean(index);
      return rset.wasNull() ? null : value ? _1_ : _0_;
    }

    private final static Short _1_ = Short.valueOf((short)1);
    private final static Short _0_ = Short.valueOf((short)0);

    final static BoolShortGetter INSTANCE = new BoolShortGetter();
  }

  @SuppressWarnings("UnnecessaryBoxing")
  static final class BoolIntGetter extends JdbcValueGetter<Integer> {
    @Override
    @Nullable
    Integer getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final boolean value = rset.getBoolean(index);
      return rset.wasNull() ? null : value ? _1_ : _0_;
    }

    private final static Integer _1_ = Integer.valueOf((int)1);
    private final static Integer _0_ = Integer.valueOf((int)0);

    final static BoolIntGetter INSTANCE = new BoolIntGetter();
  }



  static final class IntBoolGetter extends JdbcValueGetter<Boolean> {
    @Override
    @Nullable
    Boolean getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final int value = rset.getInt(index);
      return rset.wasNull() ? null : value > 0;
    }


    static final IntBoolGetter INSTANCE = new IntBoolGetter();
  }



  static final class ByteGetter extends JdbcValueGetter<Byte> {
    @Override
    @Nullable
    Byte getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final byte value = rset.getByte(index);
      return rset.wasNull() ? null : value;
    }


    final static ByteGetter INSTANCE = new ByteGetter();
  }


  static final class ShortGetter extends JdbcValueGetter<Short> {
    @Override
    @Nullable
    Short getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final short value = rset.getShort(index);
      return rset.wasNull() ? null : value;
    }


    final static ShortGetter INSTANCE = new ShortGetter();
  }



  static final class IntGetter extends JdbcValueGetter<Integer> {
    @Override
    @Nullable
    Integer getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final int value = rset.getInt(index);
      return rset.wasNull() ? null : value;
    }


    final static IntGetter INSTANCE = new IntGetter();
  }



  static final class LongGetter extends JdbcValueGetter<Long> {
    @Override
    @Nullable
    Long getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final long value = rset.getLong(index);
      return rset.wasNull() ? null : value;
    }


    final static LongGetter INSTANCE = new LongGetter();
  }



  static final class FloatGetter extends JdbcValueGetter<Float> {
    @Override
    @Nullable
    Float getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final float value = rset.getFloat(index);
      return rset.wasNull() ? null : value;
    }


    final static FloatGetter INSTANCE = new FloatGetter();
  }



  static final class DoubleGetter extends JdbcValueGetter<Double> {
    @Override
    @Nullable
    Double getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final double value = rset.getDouble(index);
      return rset.wasNull() ? null : value;
    }


    final static DoubleGetter INSTANCE = new DoubleGetter();
  }



  static final class BigDecimalGetter extends JdbcValueGetter<BigDecimal> {
    @Override
    @Nullable
    BigDecimal getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      return rset.getBigDecimal(index);
    }


    final static BigDecimalGetter INSTANCE = new BigDecimalGetter();
  }



  static final class StringGetter extends JdbcValueGetter<String> {
    @Override
    @Nullable
    String getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      return rset.getString(index);
    }


    static final StringGetter INSTANCE = new StringGetter();
  }



  static final class CharGetter extends JdbcValueGetter<Character> {
    @Override
    @Nullable
    Character getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      final String str = rset.getString(index);
      if (rset.wasNull() || str == null || str.length() == 0) {
        return null;
      }
      return str.charAt(0);
    }


    final static CharGetter INSTANCE = new CharGetter();
  }



  static final class JavaDateGetter extends JdbcValueGetter<java.util.Date> {
    @Override
    @Nullable
    java.util.Date getValue(@NotNull final ResultSet rset, final int index)  throws SQLException {
      final Timestamp timestamp = rset.getTimestamp(index);
      return rset.wasNull() ? null : new java.util.Date(timestamp.getTime());
    }


    final static JavaDateGetter INSTANCE = new JavaDateGetter();
  }


  static final class DateGetter extends JdbcValueGetter<java.sql.Date> {
    @Override
    @Nullable
    java.sql.Date getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      return rset.getDate(index);
    }


    final static DateGetter INSTANCE = new DateGetter();
  }


  static final class TimestampGetter extends JdbcValueGetter<Timestamp> {
    @Override
    @Nullable
    Timestamp getValue(@NotNull final ResultSet rset, final int index)  throws SQLException {
      return rset.getTimestamp(index);
    }


    final static TimestampGetter INSTANCE = new TimestampGetter();
  }


  static final class TimeGetter extends JdbcValueGetter<java.sql.Time> {
    @Override
    @Nullable
    java.sql.Time getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      return rset.getTime(index);
    }


    final static TimeGetter INSTANCE = new TimeGetter();
  }



  static final class ObjectGetter extends JdbcValueGetter<Object> {
    @Override
    @Nullable
    Object getValue(@NotNull final ResultSet rset, final int index) throws SQLException {
      return rset.getObject(index);
    }


    static final ObjectGetter INSTANCE = new ObjectGetter();
  }


  static final class DumbNullGetter extends JdbcValueGetter<Object> {
    @Nullable
    @Override
    Object getValue(@NotNull final ResultSet rset, final int index) {
      return null;
    }


    static final DumbNullGetter INSTANCE = new DumbNullGetter();
  }

}