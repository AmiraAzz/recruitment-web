package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;

public class Student extends Member {
	/**
	 * Level year of the student.
	 */
	private int year;
	/**
	 * Price per day in cents.
	 */
	private static final float PRICE_DAY = 0.1f;
	/**
	 * Price per day in delay period.
	 */
	private static final float PRICE_DELAY = 0.1f;

	public Student(int year, float money) {
		super(money);
		this.year = year;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void payBook(long numberOfDays) {
		float price = 0f;
		if (this.year == 1) {

			if (numberOfDays > 30) {
				price = (numberOfDays - 30) * PRICE_DELAY + 15 * PRICE_DAY;
			} else if (numberOfDays > 15) {
				price = (numberOfDays - 15) * PRICE_DAY;
			}
		} else {
			if (numberOfDays > 30) {
				price = (numberOfDays - 30) * PRICE_DELAY + 30 * PRICE_DAY;
			} else {
				price = numberOfDays * PRICE_DAY;
			}
		}

		this.setWallet(this.getWallet() - price);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<Book> findLateBooks() {
		return this.borrowedBooks.stream()
				.filter(book -> LocalDate.now().isAfter(BookRepository.findBorrowedBookDate(book).plusMonths(1L)))
				.collect(Collectors.toList());
	}

}
