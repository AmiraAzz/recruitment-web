package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

/**
 * An implementation class of the interface {@link Library}
 *
 */
public class LibraryImpl implements Library {

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt)
			throws HasLateBooksException, NoAvailableBookException {

		if (!member.findLateBooks().isEmpty()) {
			throw new HasLateBooksException();
		}

		Book bookToBorrow = BookRepository.findBook(isbnCode);

		BookRepository.saveBookBorrow(bookToBorrow, borrowedAt);
		member.getBorrowedBooks().add(bookToBorrow);
		return bookToBorrow;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public void returnBook(Book book, Member member) {

		final long numberOfDays = ChronoUnit.DAYS.between(BookRepository.findBorrowedBookDate(book), LocalDate.now());

		member.payBook(numberOfDays);
		BookRepository.returnBook(book);
		member.getBorrowedBooks().remove(book);

	}

}
