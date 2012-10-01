package gov.nasa.pds.objectAccess.table;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a field adapter for binary bit fields.
 */
public class BitFieldAdapter implements FieldAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BitFieldAdapter.class);

	/** A long constant that has all bits on. */
	private static final long LONG_ALL_BITS_ONE = 0xFFFFFFFFFFFFFFFFL;
	
	private boolean isSigned;

	/**
	 * Creates a new bit field adapter with given signed-ness.
	 * 
	 * @param isSigned true, if the bit field is signed
	 */
	public BitFieldAdapter(boolean isSigned) {
		this.isSigned = isSigned;
	}

	@Override
	public String getString(byte[] buf, int offset, int length, int startBit, int stopBit) {
		return Long.toString(getFieldValue(buf, offset, length, startBit, stopBit));
	}

	@Override
	public String getString(byte[] buf, int offset, int length, int startBit,
			int stopBit, Charset charset) {
		return Long.toString(getFieldValue(buf, offset, length, startBit, stopBit));
	}

	@Override
	public byte getByte(byte[] buf, int offset, int length, int startBit, int stopBit) {
		long value = getFieldValue(buf, offset, length, startBit, stopBit);
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			// TODO: log with logger
			throw new NumberFormatException("Binary integer value out of range for byte (" + value + ")");
		}
		
		return (byte) value;
	}

	@Override
	public short getShort(byte[] buf, int offset, int length, int startBit, int stopBit) {
		long value = getFieldValue(buf, offset, length, startBit, stopBit);
		if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
			throw new NumberFormatException("Binary integer value out of range for short (" + value + ")");
		}
		
		return (short) value;
	}

	@Override
	public int getInt(byte[] buf, int offset, int length, int startBit, int stopBit) {
		long value = getFieldValue(buf, offset, length, startBit, stopBit);
		if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
			throw new NumberFormatException("Binary integer value out of range for int (" + value + ")");
		}
		
		return (int) value;
	}

	@Override
	public long getLong(byte[] buf, int offset, int length, int startBit, int stopBit) {
		return getFieldValue(buf, offset, length, startBit, stopBit);
	}

	@Override
	public float getFloat(byte[] buf, int offset, int length, int startBit, int stopBit) {
		return getFieldValue(buf, offset, length, startBit, stopBit);
	}

	@Override
	public double getDouble(byte[] buf, int offset, int length, int startBit, int stopBit) {
		return getFieldValue(buf, offset, length, startBit, stopBit);
	}

	private long getFieldValue(byte[] b, int offset, int length, int startBit, int stopBit) {
		if (startBit < 0) {
			String msg = "Start bit is negative (" + startBit + ")";
			LOGGER.error(msg);
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		if (stopBit >= length * Byte.SIZE) {
			String msg = "Stop bit past end of packed field (" + stopBit + " > " + (length * Byte.SIZE - 1) + ")";
			LOGGER.error(msg);
			throw new ArrayIndexOutOfBoundsException(msg);							
		}
		if (stopBit - startBit + 1 > Long.SIZE) {
			String msg = "Bit field is wider than long (" + (stopBit-startBit+1) + " > " + Long.SIZE + ")";
			LOGGER.error(msg);
			throw new IllegalArgumentException(msg);			
		}
		
		int startByte = startBit / Byte.SIZE;
		int stopByte = stopBit / Byte.SIZE;
		
		if (stopByte-startByte+1 > Long.SIZE / Byte.SIZE) {
			String msg = "Bit field spans bytes that are wider than a long "
				+ "(" + (stopByte-startByte+1) + " > " + (Long.SIZE / Byte.SIZE) + ")";
			LOGGER.error(msg);
			throw new NumberFormatException(msg);
		}
		
		long bytesValue = getBytesAsLong(b, offset, startByte, stopByte);
		
		// Now shift right to get rid of the extra bits.
		int extraRightBits = (stopByte + 1)*Byte.SIZE - stopBit - 1;
		long shiftedValue = bytesValue >> extraRightBits;
		
		return rightmostBits(shiftedValue, stopBit-startBit+1, isSigned);
	}
	
	// Default scope, for unit testing.
	static long rightmostBits(long value, int nBits, boolean isSigned) {
		long mask = 0;
		if (nBits > 0) {
			mask = LONG_ALL_BITS_ONE >>> (Long.SIZE - nBits);
		}
		long maskedValue = value & mask;
		
		// Now sign-extend, if signed.
		if (isSigned && nBits < Long.SIZE) {
			long signBit = 1L << (nBits - 1);
			if ((maskedValue & signBit) != 0) {
				maskedValue |= (LONG_ALL_BITS_ONE << nBits);
			}
		}
		
		return maskedValue;
	}
	
	static long getBytesAsLong(byte[] source, int off, int startByte, int stopByte) {
		long value = 0;
		
		for (int i=off+startByte; i <= off+stopByte; ++i) {
			value = (value << Byte.SIZE) | (source[i] & 0xFF);
		}
		
		return value;
	}

}