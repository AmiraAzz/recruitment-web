package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;

public class Resident extends Member {
	/**
	 * Price per day in cents.
	 */
	private static final float PRICE_DAY = 0.1f;
	/**
	 * Price per day in delay period.
	 */
	private static final float PRICE_DELAY = 0.2f;

	public Resident(float money) {
		super(money);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void payBook(long numberOfDays) {
		float price;

		if (numberOfDays < 60) {
			// resident not late.
			price = numberOfDays * PRICE_DAY;
		} else {
			// resident is late.
			price = (numberOfDays - 60) * PRICE_DELAY + 60 * PRICE_DAY;
		}
		this.setWallet(this.getWallet() - price);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public List<Book> findLateBooks() {
		return this.borrowedBooks.stream()
				.filter(book -> LocalDate.now().isAfter(BookRepository.findBorrowedBookDate(book).plusMonths(2L)))
				.collect(Collectors.toList());
	}
}
