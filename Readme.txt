Add the followings settings to .m2/settings.xml:
			<seapay.oracle.url>jdbc:oracle:thin:@localhost:1521:xe</seapay.oracle.url>
			<seapay.oracle.username>IB</seapay.oracle.username>
			<seapay.oracle.password>IB</seapay.oracle.password>

			<seapay.oracle.stage.url>jdbc:oracle:thin:@localhost:1521:xe</seapay.oracle.stage.url>
			<seapay.oracle.stage.username>IB</seapay.oracle.stage.username>
			<seapay.oracle.stage.password>IB</seapay.oracle.stage.password>
Note: You should create users, schema and change url if it is needed according to your development box settings.
