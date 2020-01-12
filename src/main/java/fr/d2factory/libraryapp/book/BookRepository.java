package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.d2factory.libraryapp.library.NoAvailableBookException;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private static Map<ISBN, Book> availableBooks = new HashMap<>();
	private static Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	/**
	 * adding a list of books.
	 * 
	 * @param books
	 *            list of {@link Book}
	 */
	public static void addBooks(List<Book> books) {
		books.stream().forEach(book -> availableBooks.put(book.getIsbn(), book));
	}

	/**
	 * clear the book repository.
	 */
	public static void clearLibrary() {
		availableBooks = new HashMap<>();
		borrowedBooks = new HashMap<>();
	}

	/**
	 * finds an available book from its ISBN.
	 * 
	 * @param isbnCode
	 *            isbnCode
	 * @return {@link Book}
	 * @throws NoAvailableBookException
	 *             thrown if no available book has been found.
	 */
	public static Book findBook(long isbnCode) throws NoAvailableBookException {
		return Optional.ofNullable(availableBooks.get(new ISBN(isbnCode))).orElseThrow(NoAvailableBookException::new);
	}

	/**
	 * save the book borrow.
	 * 
	 * @param book
	 *            to borrow
	 * @param borrowedAt
	 *            date of the borrow.
	 */
	public static void saveBookBorrow(Book book, LocalDate borrowedAt) {

		availableBooks.remove(book.getIsbn());
		borrowedBooks.put(book, borrowedAt);
	}

	/**
	 * finds the borrowed book date.
	 * 
	 * @param book
	 * @return {@link LocalDate}
	 */
	public static LocalDate findBorrowedBookDate(Book book) {
		return borrowedBooks.get(book);
	}

	/**
	 * return a book to the list of available books.
	 * 
	 * @param book
	 */
	public static void returnBook(Book book) {
		availableBooks.put(book.getIsbn(), book);
		borrowedBooks.remove(book);
	}

	/**
	 * private constructor.
	 */
	private BookRepository() {
		super();
	}

}
