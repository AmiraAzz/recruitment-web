package fr.d2factory.libraryapp.book;

import java.util.Objects;

/**
 * A simple representation of an ISBN.
 */
public class ISBN {
	private long isbnCode;

	/*
	 * Getters and Setters
	 */
	public long getIsbnCode() {
		return isbnCode;
	}

	public void setIsbnCode(long isbnCode) {
		this.isbnCode = isbnCode;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(Object isbn) {

		if (this == isbn)
			return true;

		return (isbn instanceof ISBN && this.isbnCode == ((ISBN) isbn).getIsbnCode());

	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(isbnCode);
	}

	/**
	 * default constructor.
	 */
	public ISBN() {
		super();
	}

	/**
	 * constructor using fields.
	 * 
	 * @param isbnCode
	 */
	public ISBN(long isbnCode) {
		this.isbnCode = isbnCode;
	}
}
