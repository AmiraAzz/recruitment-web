package fr.d2factory.libraryapp.library;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
	private Library library = new LibraryImpl();

	@BeforeEach
	void setup() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File booksJson = new File("src/test/resources/books.json");
		List<Book> books = Arrays.asList(mapper.readValue(booksJson, Book[].class));
		BookRepository.clearLibrary();
		BookRepository.addBooks(books);
	}

	@Test
	void member_can_borrow_a_book_if_book_is_available() {
		Member student1 = new Student(2, 50);
		long isbnCode = 46578964513L;
		try {

			Book book = library.borrowBook(isbnCode, student1, LocalDate.now());
			Assertions.assertNotNull(book);
			Assertions.assertNotNull(BookRepository.findBorrowedBookDate(book));

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}

	}

	@Test
	void borrowed_book_is_no_longer_available() {
		Member student1 = new Student(2, 50);
		Member resident1 = new Resident(50);
		long isbnCode = 46578964513L;
		try {

			library.borrowBook(isbnCode, student1, LocalDate.now());

			library.borrowBook(isbnCode, resident1, LocalDate.now());
			Assertions.fail("an exception was expected to be thrown");

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.assertEquals(resident1.getBorrowedBooks().isEmpty(), true);
			Assertions.assertEquals(student1.getBorrowedBooks().isEmpty(), false);

		}
	}

	@Test
	void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		Member resident = new Resident(50);
		long isbnCode = 46578964513L;
		try {

			Book book = library.borrowBook(isbnCode, resident, LocalDate.now().minusDays(10));
			library.returnBook(book, resident);

			Assertions.assertNotNull(resident.getWallet());
			Assertions.assertEquals(resident.getWallet(), 49f);

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}

	}

	@Test
	void students_pay_10_cents_the_first_30days() {
		Member student = new Student(2, 50);
		long isbnCode = 46578964513L;
		try {

			Book book = library.borrowBook(isbnCode, student, LocalDate.now().minusDays(30));
			library.returnBook(book, student);

			Assertions.assertNotNull(student.getWallet());
			Assertions.assertEquals(student.getWallet(), 47f);

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}
	}

	@Test
	void students_in_1st_year_are_not_taxed_for_the_first_15days() {
		Member student = new Student(1, 50);
		long isbnCode = 46578964513L;
		try {

			Book book = library.borrowBook(isbnCode, student, LocalDate.now().minusDays(30));
			library.returnBook(book, student);

			Assertions.assertNotNull(student.getWallet());
			Assertions.assertEquals(student.getWallet(), 48.5f);

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}
	}

	@Test
	void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		Member resident = new Resident(50);
		long isbnCode = 46578964513L;
		try {

			Book book = library.borrowBook(isbnCode, resident, LocalDate.now().minusDays(70));
			library.returnBook(book, resident);

			Assertions.assertNotNull(resident.getWallet());
			Assertions.assertEquals(resident.getWallet(), 42f);

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}
	}

	@Test
	void members_cannot_borrow_book_if_they_have_late_books() {
		Member resident = new Resident(50);
		long isbnCode1 = 46578964513L;
		long isbnCode2 = 3326456467846L;

		try {

			library.borrowBook(isbnCode1, resident, LocalDate.now().minusDays(65));
			library.borrowBook(isbnCode2, resident, LocalDate.now());
			Assertions.fail("an exception was expected to be thrown");

		} catch (HasLateBooksException e) {

			Assertions.assertNotNull(resident.findLateBooks());
			Assertions.assertEquals(resident.findLateBooks().get(0).getIsbn(), new ISBN(isbnCode1));
			Assertions.assertEquals(resident.getBorrowedBooks().get(0).getIsbn(), new ISBN(isbnCode1));
		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}
	}

	@Test
	void members_can_borrow_book_if_they_have_late_books_and_they_return_them() {
		Member student = new Student(1, 50);
		long isbnCode1 = 46578964513L;
		long isbnCode2 = 3326456467846L;

		try {

			Book book = library.borrowBook(isbnCode1, student, LocalDate.now().minusDays(65));
			library.returnBook(book, student);
			Book book2 = library.borrowBook(isbnCode2, student, LocalDate.now());

			Assertions.assertNotNull(book);
			Assertions.assertNotNull(book2);
			Assertions.assertEquals(student.getBorrowedBooks().size(), 1);
			Assertions.assertNull(BookRepository.findBorrowedBookDate(book));
			Assertions.assertNotNull(BookRepository.findBorrowedBookDate(book2));

		} catch (HasLateBooksException e) {
			Assertions.fail("the member has no late books");

		} catch (NoAvailableBookException e) {
			Assertions.fail("the book is available");
		}
	}
}
