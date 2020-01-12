package fr.d2factory.libraryapp.member;

import java.util.ArrayList;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member {
	/**
	 * An initial sum of money the member has
	 */
	protected float wallet;
	/**
	 * the list of borrowed books for the member.
	 */
	protected List<Book> borrowedBooks = new ArrayList<>();

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays
	 *            the number of days they kept the book
	 */
	public abstract void payBook(long numberOfDays);

	/**
	 * find the list of late books.
	 * 
	 * @return list of {@link Book}
	 */
	public abstract List<Book> findLateBooks();

	/*
	 * Getters and Setters.
	 */
	public List<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<Book> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	/**
	 * Constructor using fields.
	 * 
	 * @param wallet
	 */
	protected Member(float wallet) {
		super();
		this.wallet = wallet;
	}
}
