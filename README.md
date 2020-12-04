# emotianali
Service of emotional assessment of messages

# Visit us
Link: https://emotianali.herokuapp.com/

# About service
The service allows to check emotional assessment of your instagram posts or other messages, if user does not have an account

The system uses IBM Watson Tone Analyzer for emotional recognition, IBM Watson Translation (if your language is not English), Instagram API for receiving captions of posts. Service required registration, data is used for internal functions and statistics, the only one transfer - between Instagram and IBM Watson TA system. 

### Emotianali supports 3* types of users:
- base user: only emotional assessment
- administrator: managing of all users and getting base statistics
- analyst: getting advanced statistics without information about posts
- *blocked user

### Supported emotions (based on Tone Analyzer):
- Anger
- Fear
- Joy
- Sadness
- Analytical
- Confident
- Tentative

### Technologies used:
- Java Spring (MVC, JPA, Security for users)
- Thymeleaf
- HTML, CSS, JS
- IBM APIs
- Instagram API
- Apache Mail Service, POI for files
- JFreeChart library
