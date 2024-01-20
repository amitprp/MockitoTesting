package ac.il.bgu.qa;

import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;


public class TestLibrary {
    private DatabaseService mockDatabaseService;
    private ReviewService mockReviewService;
    private NotificationService mockNotificationService;
    private Library library;
    private User user;
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
        Assertions.assertThrows(IllegalArgumentException.class,() -> library.addBook(null));
    }

    @Test
    public void givenISBNIsNotValid_whenAddBook_ThrowIllegalArgException(){

        when(book.getISBN()).thenReturn("1234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
    }

    @Test
    public void givenTitleIsNull_whenAddBook_ThrowIllegalArgException(){
        when(book.getTitle()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
    }

    @Test
    public void givenTitleIsNotValid_whenAddBook_ThrowIllegalArgException(){
        when(book.getTitle()).thenReturn("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
    }

    @Test
    public void givenAuthorIsNotValid_whenAddBook_ThrowIllegalArgException(){

        when(book.getAuthor()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
    }

    @Test
    public void givenBookIsBorrowed_whenAddBook_ThrowIllegalArgException(){

        when(book.isBorrowed()).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.addBook(book));
    }

    @Test
    public void givenBookIsInDatabase_whenAddBook_ThrowIllegalArgException(){
        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn("Amit");

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(book);

        Assertions.assertThrows(IllegalArgumentException.class,() -> library.addBook(null));
    }

    @Test
    public void givenBookIsValid_whenAddBook_bookAddedSuccessfully(){

        String ISBN = "978-965-231-157-3";
        when(book.getISBN()).thenReturn(ISBN);
        when(book.getTitle()).thenReturn("Heroes");
        when(book.getAuthor()).thenReturn("Amit");

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);

        library.addBook(book);

        verify(mockDatabaseService).addBook(ISBN, book);
    }
    //endregion

    //region registerUser


    //        } else if (user.getNotificationService() == null) {
    //            throw new IllegalArgumentException("Invalid notification service.");
    //        }

    @Test
    public void givenUserIsNull_whenRegisterUser_ThrowIllegalArgException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));
    }

    @Test
    public void givenUserIDIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
    }

    @Test
    public void givenUserIDIsNotValid_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getId()).thenReturn("1234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
    }


    @Test
    public void givenUserNameIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getName()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
    }

    @Test
    public void givenUserNameIsNotValid_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getName()).thenReturn("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
    }

    @Test
    public void givenNotificationServiceOfUserIsNull_whenRegisterUser_ThrowIllegalArgException(){

        when(user.getNotificationService()).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
    }

    @Test
    public void givenUserIsAlreadyRegistered_whenRegisterUser_ThrowIllegalArgException(){

        String ID = "123456789012";


        when(user.getId()).thenReturn(ID);
        when(user.getName()).thenReturn("Amit");
        when(user.getNotificationService()).thenReturn(mock(NotificationService.class));


        when(mockDatabaseService.getUserById(ID)).thenReturn(user);

        Assertions.assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));
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

    //endregion
}
