package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


public class TestLibrary {
    private DatabaseService mockDatabaseService;
    private ReviewService mockReviewService;
    private NotificationService mockNotificationService;
    private Library library;
    private User user;
    @Mock
    private Book book;
    @BeforeEach
    public void setUp(){
        mockDatabaseService = mock(DatabaseService.class);
        mockReviewService = mock(ReviewService.class);
        user = mock(User.class);
        book = mock(Book.class);
        library = new Library(mockDatabaseService, mockReviewService);
    }

    //region AddBook
    @Test
    public void givenBookIsNull_whenAddBook_ThrowIllegalArgException(){
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,() -> library.addBook(null));
        Assertions.assertEquals(e.getMessage(), "Invalid book.");
    }

    @Test
    public void givenISBNIsNotValid_whenAddBook_ThrowIllegalArgException(){

        when(book.getISBN()).thenReturn("1234");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Invalid ISBN.");
    }

    @Test
    public void givenTitleIsNull_whenAddBook_ThrowIllegalArgException(){
        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn(null);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Invalid title.");
    }

    @Test
    public void givenTitleIsNotValid_whenAddBook_ThrowIllegalArgException(){
        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Invalid title.");
    }

    @Test
    public void givenAuthorIsNotValid_whenAddBook_ThrowIllegalArgException(){

        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn(null);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Invalid author.");

    }

    @Test
    public void givenBookIsBorrowed_whenAddBook_ThrowIllegalArgException(){
        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn("Amit");
        when(book.isBorrowed()).thenReturn(true);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Book with invalid borrowed state.");
    }

    @Test
    public void givenBookIsInDatabase_whenAddBook_ThrowIllegalArgException(){

        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn("Amit");
        when(book.isBorrowed()).thenReturn(false);

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class,() -> library.addBook(book));
        Assertions.assertEquals(e.getMessage(), "Book already exists.");
    }

    @Test
    public void givenBookIsValid_whenAddBook_bookAddedSuccessfully(){

        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn("Amit");
        when(book.isBorrowed()).thenReturn(false);

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        library.addBook(book);

        verify(mockDatabaseService).addBook(ISBN, book);
    }
    //endregion

    //region registerUser


    @Test
    public void givenUserIsNull_whenRegisterUser_ThrowIllegalArgException(){
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));
        Assertions.assertEquals(e.getMessage(), "Invalid user.");
    }

    @Test
    public void givenUserIDIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn(null);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }

    @Test
    public void givenUserIDIsNotValid_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn("1234");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }


    @Test
    public void givenUserNameIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn("318434123789");
        when(user.getName()).thenReturn(null);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "Invalid user name.");
    }

    @Test
    public void givenUserNameIsNotValid_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn("318434123789");
        when(user.getName()).thenReturn("");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "Invalid user name.");
    }

    @Test
    public void givenNotificationServiceOfUserIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn("318434123789");
        when(user.getName()).thenReturn("Amit");
        when(user.getNotificationService()).thenReturn(null);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "Invalid notification service.");
    }

    @Test
    public void givenUserIsAlreadyRegistered_whenRegisterUser_ThrowIllegalArgException(){

        String ID = "123456789012";


        when(user.getId()).thenReturn(ID);
        when(user.getName()).thenReturn("Amit");
        when(user.getNotificationService()).thenReturn(mock(NotificationService.class));


        when(mockDatabaseService.getUserById(ID)).thenReturn(user);

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
        Assertions.assertEquals(e.getMessage(), "User already exists.");
    }

    @Test
    public void givenUserIsValid_whenRegisterUser_userAddedSuccessfully(){

        String ID = "123456789012";


        when(user.getId()).thenReturn(ID);
        when(user.getName()).thenReturn("Amit");
        when(user.getNotificationService()).thenReturn(mock(NotificationService.class));


        when(mockDatabaseService.getUserById(ID)).thenReturn(null);

        library.registerUser(user);

        verify(mockDatabaseService).registerUser(ID,user);
    }



    //endregion

    //region BorrowBook
    @Test
    public void givenISBNIsNotValid_whenBorrowBook_ThrowInvalidISBNMsgAndException(){
        String ID = "318434123789";
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.borrowBook(null, ID));
        Assertions.assertEquals(e.getMessage(), "Invalid ISBN.");

    }

    @Test
    public void givenNoBookIsFound_whenBorrowBook_ThrowBookNotFoundException(){
        String ID = "318434123789";
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        BookNotFoundException e = Assertions.assertThrows(BookNotFoundException.class, () -> library.borrowBook(ISBN, ID));
        Assertions.assertEquals(e.getMessage(), "Book not found!");

    }

    @Test
    public void givenUserIDIsNotValid_whenBorrowBook_ThrowInvalidUserIdMsgAndException(){
        String ID = "318434123";
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.borrowBook(ISBN, ID));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");

    }

    @Test
    public void givenUserIDIsNull_whenBorrowBook_ThrowInvalidUserIdMsgAndException(){
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.borrowBook(ISBN, null));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");

    }

    @Test
    public void givenUserIDIsNotRegistered_whenBorrowBook_ThrowUserNotRegisteredException(){
        String ID = "318434123789";
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(null);

        UserNotRegisteredException e = Assertions.assertThrows(UserNotRegisteredException.class, () -> library.borrowBook(ISBN, ID));
        Assertions.assertEquals(e.getMessage(), "User not found!");

    }

    @Test
    public void givenBookIsBorrowed_whenBorrowBook_ThrowBookAlreadyBorrowedException(){
        String ID = "318434123789";
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(user);

        when(book.isBorrowed()).thenReturn(true);

        BookAlreadyBorrowedException e = Assertions.assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook(ISBN, ID));
        Assertions.assertEquals(e.getMessage(), "Book is already borrowed!");

    }

    @Test
    public void givenDataIsValid_whenBorrowBook_BorrowBookSuccessfully(){
        String ID = "318434123789";
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(user);

        when(book.isBorrowed()).thenReturn(false);

        library.borrowBook(ISBN, ID);

        verify(book).borrow();
        verify(mockDatabaseService).borrowBook(ISBN,ID);

    }

                                /// test returnBook ///
    @Test
    public void invalidISNB_throw_exception(){
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.returnBook(null));
        Assertions.assertEquals(e.getMessage(), "Invalid ISBN.");
    }

    @Test
    public void wrongISNB_book_notFound(){
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        BookNotFoundException e = Assertions.assertThrows(BookNotFoundException.class, () -> library.returnBook(ISBN));
        Assertions.assertEquals(e.getMessage(), "Book not found!");
    }

    @Test
    public void rightISNB_book_WasNotBorrowed() {
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(book.isBorrowed()).thenReturn(false);

        BookNotBorrowedException e = Assertions.assertThrows(BookNotBorrowedException.class, () -> library.returnBook(ISBN));
        Assertions.assertEquals(e.getMessage(), "Book wasn't borrowed!");
    }

    @Test
    public void rightISNB_book_Borrowed_checkReturn() {
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(book.isBorrowed()).thenReturn(true);
        library.returnBook(ISBN);

        verify(book).returnBook();
    }

    @Test
    public void rightISNB_book_Borrowed_check_DataBase_update() {
        String ISBN = "978-965-231-157-3";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(book.isBorrowed()).thenReturn(true);
        library.returnBook(ISBN);

        verify(mockDatabaseService).returnBook(ISBN);
    }

                                /// Test GetBookByISBN ///

    @Test
    public void nullISNB_throw_IllegalArgumentException(){
        String ID = "318434123321";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN(null,ID));
        Assertions.assertEquals(e.getMessage(), "Invalid ISBN.");
    }

    @Test
    public void given_nullID_throw_IllegalArgumentException(){
        String ISBN = "978-965-231-157-3";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN(ISBN,null));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }

    @Test
    public void given_WrongSizeID_throw_IllegalArgumentException(){
        String ISBN = "978-965-231-157-3";
        String ID = "31843412332";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }

    @Test
    public void given_rightIDAndISBN_throw_BookNotFoundException(){
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        BookNotFoundException e = Assertions.assertThrows(BookNotFoundException.class, () -> library.getBookByISBN(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "Book not found!");
    }

    @Test
    public void given_rightIDAndISBN_throw_BookAlreadyBorrowedException() {
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(book.isBorrowed()).thenReturn(true);

        BookAlreadyBorrowedException e = Assertions.assertThrows(BookAlreadyBorrowedException.class, () -> library.getBookByISBN(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "Book was already borrowed!");
    }

                                /// Test notifyUserWithBookReviews ///

    @Test
    public void notify_given_nullISNB_throw_IllegalArgumentException(){
        String ID = "318434123321";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(null,ID));
        Assertions.assertEquals(e.getMessage(), "Invalid ISBN.");
    }

    @Test
    public void notify_given_nullID_throw_IllegalArgumentException(){
        String ISBN = "978-965-231-157-3";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN,null));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }

    @Test
    public void notify_givenWrongSizeID_throw_IllegalArgumentException(){
        String ISBN = "978-965-231-157-3";
        String ID = "31843412332";

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "Invalid user Id.");
    }

    @Test
    public void notify_givenValidIDAndISBN_throw_BookNotFoundException(){
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        BookNotFoundException e = Assertions.assertThrows(BookNotFoundException.class, () -> library.notifyUserWithBookReviews(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "Book not found!");
    }

    @Test
    public void notify_givenValidIDAndISBN_throw_UserNotRegisteredException(){
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(null);

        UserNotRegisteredException e = Assertions.assertThrows(UserNotRegisteredException.class, () -> library.notifyUserWithBookReviews(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "User not found!");
    }

    @Test
    public void notify_givenRightIDAndISBN_checkClosedConnection(){
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";
        List<String> reviews = new ArrayList<>();
        reviews.add("review");

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(user);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(reviews);

        library.notifyUserWithBookReviews(ISBN,ID);

        verify(mockReviewService).close();
    }

    @Test
    public void notify_givenRightIDAndISBN_throw_ReviewServiceUnavailableException(){
        String ISBN = "978-965-231-157-3";
        String ID = "318434123321";

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);
        when(mockDatabaseService.getUserById(ID)).thenReturn(user);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(null);

        NoReviewsFoundException e = Assertions.assertThrows(NoReviewsFoundException.class, () -> library.notifyUserWithBookReviews(ISBN,ID));
        Assertions.assertEquals(e.getMessage(), "No reviews found!");
    }

    //TODO: add test to check the last exception in notifyUserWithBookReviews


    //endregion
}
