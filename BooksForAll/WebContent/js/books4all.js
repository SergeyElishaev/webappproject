var book4allApp = angular.module('books4all',[]);


book4allApp.controller('mainController', ['$scope', function($scope){
	$scope.view = 'view/home.html';
	
	$scope.setView = function(view){
		$scope.view = view;
	}
	$scope.getView = function(){
		if($scope.isUserLoggedIn == true){
			$scope.view = 'view/home.html';			
		}
	}
}]);


book4allApp.controller('loginController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
	
	$scope.pwdPlaceHolder = "Password (Hint: Passw0rd)";
	$rootScope.loginWarning = null;
	$rootScope.isUserLoggedIn = false;

	// on login button click
	$scope.login = function() {
        $http({
            url : 'login',
            method : "POST",
            data : {
                'username' : $scope.loginUsername,
                'password' : $scope.loginPassword
            }
        }).then(function(response) {
            
            if(response.data.status == "LoginSuccess"){
            	$rootScope.user = response.data.object;
            	$rootScope.isUserLoggedIn = true;
                $scope.setView('view/home.html');
                $rootScope.loginWarning = null;
            }
            else if(response.data.status == "LoginFailure"){
            	$rootScope.loginWarning = response.data.details;
            }
        });
    }
    
    // on logout button click
    $scope.logout = function(){
    	$rootScope.user = null;
    	$rootScope.isUserLoggedIn = false;
        $scope.setView('view/home.html');
        $rootScope.loginWarning = null;
    }
}]);
//=================================== EO loginController ====================================================

book4allApp.controller('registrationController', ['$rootScope', '$scope', '$http',  function($rootScope, $scope, $http) {
	$scope.regWarningMessage = null;
	
	//on registration submit
	$scope.register = function(){
		
		$http({
	        url : 'registration',
	        method : "POST",
	        data : {
	        	'Username': $scope.regUsername,
	    		'Email': $scope.regEmail,
	    		'Phone': $scope.regPhone,
	    		'Nickname': $scope.regNickname,
	    		'Password': $scope.regPassword,
	    		'Description': $scope.regDescription,
	    		'PhotoURL': $scope.regPhotoUrl,
	    		'AddrStreet': $scope.regAddrStreet,
	    		'AddrNumber': $scope.regAddrNumber,
	    		'AddrCity': $scope.regAddrCity,
	    		'AddrZip': $scope.regAddrZip
	        }
	    }).then(function(response) {
	        
	        if(response.data.status == "RegistrationSuccess"){
                $scope.setView('view/home.html');
                $rootScope.registrationSuccess = response.data.details;
            }
            else{
            	$rootScope.registrationWarnings = response.data;
            }
	    });
	}
}]);
//=================================== EO registrationController ====================================================

book4allApp.controller('booksController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	$scope.books = null;
	$scope.bookInfo = null;
	$scope.userInfo = null;
	$scope.isLikeTooltipOpen = [false,false,false,false,false,false,false,false,false,false,false];
	
	$scope.toggleLikesTooltip = function(bookId){
		if($scope.isLikeTooltipOpen[bookId] == true){
			$(".store-book-likes-tooltip-"+bookId + " .store-book-likes-tooltip ").hide();
			$scope.isLikeTooltipOpen[bookId] = false;
		}
		else{
			$(".store-book-likes-tooltip-"+bookId + " .store-book-likes-tooltip ").show();
			$scope.isLikeTooltipOpen[bookId] = true;
		}
	}
	//click on user nickname
	$scope.showUserInfo = function(UserId, BookId){
		$http({
	        url : 'admin/showUserInfo',
	        method : "POST",
	        data : {
	        	'value': UserId
	        }
	    }).then(function(response) {
	    	if(response.data.status == "Success"){
	    		$scope.userInfo = response.data.object; 
	    		$(".store-book-likes-tooltip-"+BookId + " ul").hide();
	    		$scope.isLikeTooltipOpen[BookId] = false;
	    		$scope.openUserInfoWindow();
	    		
	    	}
	    });
	}
	
	$scope.openUserInfoWindow = function(){
		$(".mask").show();
		$(".user-info-window").show(function(){
			$(".user-info-window").click(function(){
				$scope.closeUserInfoWindow();
			});
		});
	}
	
	$scope.closeUserInfoWindow = function(){
		$(".mask").hide();
		$(".user-info-window").hide();
	}
	
	
	$scope.showBookInfo = function(book){
		$(".mask").show();
		$(".mybooks-book-info").show();
		$scope.bookInfo = book;
		
	}
	$scope.closeBookInfo = function(){
		$(".mask").hide();
		$(".mybooks-book-info").hide();
		$scope.bookInfo = null;
	}
	
	$http({
        url : 'books/allBooks',
        method : "POST",
        data : {}
    }).then(function(response) {
        $scope.books = response.data.object;
    });
}]);
//=================================== EO booksController ====================================================

book4allApp.controller('storeController', ['$rootScope', '$scope', '$http', function($rootScope, $scope, $http) {
	$scope.newBooks = null;
	$scope.myBooks = null;
	$scope.bookToBuy = null;
	$scope.validationSucceed = false;
	$scope.creditValidationError = null;
	
	
	$scope.openPaymentWindow = function(bookId){
		$scope.bookToBuy = bookId;
		$(".mask").show();
		$(".store-payment-form").show();
	}
	
	$scope.closePaymentWindow = function(){
		$scope.bookToBuy = null;
		$(".mask").hide();
		$(".store-payment-form").hide();
	}
	
	$scope.buy = function(){
		
		
		$http({
	        url : 'books/validateCredit',
	        method : "POST",
	        data : {
	        	'FullName': $scope.payFullName,
	        	'CardType': $scope.payCardType,
	        	'CardNumber': $scope.payCardNumber,
	        	'ExpiryMonth': $scope.payExpiryMonth,
	        	'ExpiryYear': $scope.payExpiryYear,
	        	'CVV': $scope.payCVV
	        	}
	    }).then(function(response) {
	    	if(response.data.status == "Success"){
	    		$http({
			        url : 'books/buyNewBook',
			        method : "POST",
			        data : {
			        	'BookId': $scope.bookToBuy,
			        	'UserId': $scope.user.Id
			        	}
			    }).then(function(response) {
			        $scope.newBooks = response.data.object;
			        $(".mask").hide();
			        $scope.closePaymentWindow();
			        $scope.setView('view/mybooks.html');
			    });
            }
            else{
            	$scope.creditValidationError = response.data.object;
            }
	    });
	}
	$scope.showBookInfo = function(book){
		$(".mask").show();
		$(".mybooks-book-info").show();
		$scope.bookInfo = book;
	}
	$scope.closeBookInfo = function(){
		$(".mask").hide();
		$(".mybooks-book-info").hide();
		$scope.bookInfo = null;
	}
	
	$http({
        url : 'books/notMyBooks',
        method : "POST",
        data : {'value': $scope.user.Id}
    }).then(function(response) {
        $scope.newBooks = response.data.object;
    });
	$http({
        url : 'books/myBooks',
        method : "POST",
        data : {'value': $scope.user.Id}
    }).then(function(response) {
        
        $scope.myBooks = response.data.object;
    });
}]);
//=================================== EO storeController ====================================================

book4allApp.controller('myBooksController', ['$rootScope', '$scope','$interval', '$http',  function($rootScope, $scope, $interval, $http) {
	$scope.books = null;
	$scope.noBooksWarning = null;
	$scope.bookToRead = null; //Path
	$scope.bookToReview = null; //book
	$rootScope.currentBook = null; //Object BookOfUser
	$rootScope.bookInfo = null;
	

	$scope.showLikesTooltip = function(bookId){
		$(".store-book-likes-tooltip-"+bookId).show("slow");
	}
	
	$scope.hideLikesTooltip = function(bookId){
		$(".store-book-likes-tooltip-"+bookId).hide("slow");
	}
	
	$scope.read = function(book){
		$(".mask").show();
		$scope.currentBook = book;
		$scope.bookToRead = book.Path;
		$(".mybooks-reading-window").show("slow", function(){
			$(".mybooks-reading-window").scrollTop(book.ScrollLocation);
		});
	}
	
	$scope.closeBook = function(){
		$(".mask").hide();
		$scope.currentBook.ScrollLocation = $(".mybooks-reading-window").scrollTop();
		$(".mybooks-reading-window").hide();
		
		$scope.scrollLocation = $(".mybooks-reading-window").scrollTop(); 
		$scope.currentBook.isOpen = true;
		
		$http({
	        url : 'books/closeBook',
	        method : "POST",
	        data : {
	        	'BookId': $scope.currentBook.BookId,
	        	'UserId': $scope.user.Id,
	        	'ScrollLocation': $scope.currentBook.ScrollLocation
	        	}
	    }).then(function(response) {

	    });
		
		$scope.currentBook = null;
	}
	
	$scope.like = function(book){
		book.Likes += 1;
		
		$http({
	        url : 'books/likeBook',
	        method : "POST",
	        data : {
	        	'BookId': book.BookId,
	        	'UserId': $scope.user.Id
	        	}
	    }).then(function(response) {
	    	
	    	if(response.data.status == "Success"){
	    		book.IsLiked = true;
	    		$(".book-id-" + book.BookId + " .store-book-like-button").hide();
	    		$(".book-id-" + book.BookId + " .store-book-unlike-button").show();
	    	}
	    	else{
	    		$scope.noBooksWarning = response.data.details;
	    		book.Likes -= 1;
	    	}
	    });
	}
	
	$scope.unlike = function(book){
		book.Likes -= 1;
		
		$http({
	        url : 'books/unlikeBook',
	        method : "POST",
	        data : {
	        	'BookId': book.BookId,
	        	'UserId': $scope.user.Id,
	        	}
	    }).then(function(response) {
	    	
	    	if(response.data.status == "Success"){
	    		book.IsLiked = false;
	    		$(".book-id-" + book.BookId + " .store-book-unlike-button").hide();
	    		$(".book-id-" + book.BookId + " .store-book-like-button").show();
	    	}
	    	else{
	    		$scope.noBooksWarning = response.data.details;
	    		book.Likes += 1;
	    	}
	    });
		
	}
	
	$scope.showBookInfo = function(book){
		$(".mask").show();
		$(".mybooks-book-info").show();
		$(".mybooks-book-info").scrollTop(0);
		$rootScope.bookInfo = book;
	}
	$scope.closeBookInfo = function(){
		$(".mask").hide();
		$(".mybooks-book-info").hide();
		$rootScope.bookInfo = null;
	}
	
	$scope.writeReview = function(book){
		$(".mask").show();
		$(".book-info-write-review-form").show();
		$scope.bookToReview = book;
		
		$scope.sendReview = function(){
			$http({
		        url : 'books/sendReview',
		        method : "POST",
		        data : {
		        	'BookId': book.BookId,
		        	'UserId': $rootScope.user.Id,
		        	'Score': $scope.revScore,
		        	'Content': $scope.revContent
		        	}
		    }).then(function(response) {
		    	
		    	if(response.data.status == "Success"){
		    		book.IsReviewed = true;
		    		//$scope.bookToReview.Reviewed = true;
		    		$scope.closeWriteReviewWindow();
		    		$scope.setView('view/mybooks.html');
		    	}
		    });
		}
	}
	$scope.closeWriteReviewWindow = function(){
		$(".mask").hide();
		$(".book-info-write-review-form").hide();
		$scope.bookToReview = null;
	}
	
	$http({
        url : 'books/myBooks',
        method : "POST",
        data : {'value': $scope.user.Id}
    }).then(function(response) {
    	
    	if(response.data.status == "BooksFound"){
	        $scope.books = response.data.object;
    	}
    	else{
    		$scope.noBooksWarning = response.data.details;
    	}
    });
	
	
}]);
//=================================== EO myBooksController ====================================================

book4allApp.controller('adminController', ['$rootScope', '$scope', '$http',  function($rootScope, $scope, $http) {
	$scope.users = null;
	$scope.transactions = null;
	$scope.reviews = null;
	
	$scope.browseUsers = function(){
		$http({
	        url : 'admin/allUsers',
	        method : "POST",
	        data : {}
	    }).then(function(response) {
	        
	        $scope.users = response.data.object;
	        
	        $(".admin-all-users").show();
	        $(".admin-recent-transactions").hide();
	        $(".admin-browse-ebooks").hide();
	        $(".admin-review-requests").hide();
	        
	    });
	}
	
	$scope.browseEbooks = function(){
		$(".admin-all-users").hide();
		$(".admin-browse-ebooks").show();
		$(".admin-recent-transactions").hide();
		$(".admin-review-requests").hide();
	}
	
	$scope.recentTransactions = function(){
		$http({
	        url : 'admin/recentTransactions',
	        method : "POST",
	        data : {}
	    }).then(function(response) {
	        
	        $scope.transactions = response.data.object;
	        
	        $(".admin-all-users").hide();
	        $(".admin-browse-ebooks").hide();
	        $(".admin-recent-transactions").show();
	        $(".admin-review-requests").hide();
	        
	    });
	}
	
	$scope.browseReviewRequests = function(){
		
		$http({
	        url : 'admin/reviewRequsts',
	        method : "POST",
	        data : {}
	    }).then(function(response) {
	        
	        $scope.reviews = response.data.object;
		
			$(".admin-all-users").hide();
	        $(".admin-browse-ebooks").hide();
	        $(".admin-recent-transactions").hide();
	        $(".admin-review-requests").show();
	    });
		
		$scope.approveReview = function(review){
			$http({
		        url : 'admin/approveReview',
		        method : "POST",
		        data : {
		        	'UserId': review.UserId,
		        	'BookId': review.BookId
		        }
		    }).then(function(response) {
		    	
		    	if(response.data.status == "Success"){
		    		$scope.browseReviewRequests();
		    	}
		    });
		}
		$scope.rejectReview = function(review){
			$http({
		        url : 'admin/rejectReview',
		        method : "POST",
		        data : {
		        	'UserId': review.UserId,
		        	'BookId': review.BookId
		        }
		    }).then(function(response) {
		    	
		    	if(response.data.status == "Success"){
		    		$scope.browseReviewRequests();
		    	}
		    });
		}
	}
	
	$scope.removeUser = function(user){
		if(confirm("Are you shure?")){
			$http({
		        url : 'admin/removeUser',
		        method : "POST",
		        data : {
		        	'value': user.Id
		        }
		    }).then(function(response) {
		    	
		    	if(response.data.status == "Success"){
		    		$scope.browseUsers();
		    	}
		    });
		}
	}
}]);
//=================================== EO adminController ====================================================
