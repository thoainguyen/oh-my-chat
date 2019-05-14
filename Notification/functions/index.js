'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotification = functions.database.ref('/Notifications/{receiverUserId}/{notificationId}')
    .onWrite((data, context) => {
        const receiverUserId = context.params.receiverUserId;
        const notificationId = context.params.notificationId;


        console.log('We have a notification to send to :', receiverUserId);


        if (!data.after.val()) {
            console.log('A notification has been deleted :', notificationId);
            return null;
        }

        const senderUserId = admin.database().ref(`/Notifications/${receiverUserId}/${notificationId}`).once('value');

        return senderUserId.then(fromUserResult => {
            const fromSenderUserId = fromUserResult.val().from;
            console.log('You have a notification from : ', fromSenderUserId);
            const userQuery = admin.database().ref(`/Users/${fromSenderUserId}/name`).once('value');
            return userQuery.then(userResult => {
                const senderUserName = userResult.val();

                const deviceToken = admin.database().ref(`/Users/${receiverUserId}/deviceToken`).once('value');

                return deviceToken.then(result => {
                    const tokenId = result.val();

                    const payload =
                    {
                        notification:
                        {
                            from: fromSenderUserId,
                            title: "New Chat Request",
                            body: `${senderUserName} want to connect with you`,
                            icon: "default"
                        }
                    };

                    return admin.messaging().sendToDevice(tokenId, payload)
                        .then(response => {
                            console.log('This was a notification feature.');
                        });
                });
            });
        });
    });