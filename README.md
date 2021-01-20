# MoneyTracker

A simple App developed using java for android devices, this app will track how much money you have spent today and this month. You can also check the money spent on a particular day and month.
This App needs permission to read SMS, it will read the sms which you received from bank and it will extract the date and amout and store it in database.

The below page is login page


![image](https://user-images.githubusercontent.com/37832523/105182465-11901300-5b53-11eb-9838-9ab1b71c49de.png)

The below page is Get started page, here if you install app, you can create password for the first time, if you already have password and have some data in app, if you click here
Your data will be lost


![image](https://user-images.githubusercontent.com/37832523/105182518-1fde2f00-5b53-11eb-90c8-7824765beff5.png)


The below page is forgot password page, here you can change password if you have forgotten it. You need to have at least one bank spending data to change password. 
You have to provide the name you have ggiven to the bank and its number which appears in SMS.

![image](https://user-images.githubusercontent.com/37832523/105182548-29679700-5b53-11eb-9527-28e8cc20d9d4.png)



the below page is where your spending details are shown, you can see it shows today's spending and this month's spending, it also shows a bank name and number,
The bank name can be given by us, but the bank number should be same as it appears in your transaction SMS, we can also check spending of a particular day and month.
at bottom, two edit texts, one is for checking spending of a particular day and one is for month. Date formats are mentioned in the hint. If app doesn't have info about 
that month or day, it will display 0.0


![image](https://user-images.githubusercontent.com/37832523/105182563-2e2c4b00-5b53-11eb-80d7-0fc0fb6900da.png)


here, im checking my spending of a particular day and a month. As information of that month is not available, it is showing 0.0


![image](https://user-images.githubusercontent.com/37832523/105183907-c119b500-5b54-11eb-939a-44fa6345bbff.png)




Here is the place where you can remove a bank name and its data, this will remove your spending data of that bank from this app.


![image](https://user-images.githubusercontent.com/37832523/105182606-3be1d080-5b53-11eb-9a9b-6cc6d459fc73.png)



this is where you add a bank, This app will track the spending of that particular bank. This is very simple, if first text area, you have to copy paste the SMS which you
receive when you make a transaction, you have to add $ just before the money. FOr ex: Rs. 118 has been debited from your account xxxx9999, then you have to make that into
Rs. $118 has been debited from your account xxxx9999. You can give any name for this bank. You have to give the bank number correctly. IN this instance
You have to give bank number as xxxx9999. Bank number is case insensitive. We do this because, when you open the app, the app will read today's sms and check if any sms has
string transaction or debited and then check if that sms has the bank number, if yes then the app will extract the money spent from that sms, we provided $ before money,
app takes it starting index and extract the amount and it will store it in database and display. If you make n transactions, app will read all transactions and store it in database
while displaying, it will sum all n transactions and display total.


![image](https://user-images.githubusercontent.com/37832523/105182616-3f755780-5b53-11eb-874e-ad66b2f44d9c.png)
