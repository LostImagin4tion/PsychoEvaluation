
# Task Flow

## Tracking

* We currently use **Wekan** for task tracking. If you need to create new ticket (task), you should declare a tag in the beginning of it's name. 

    For this project tag is `[MOBILE-*]`. You should replace asterisk with ticket number. For example, **[MOBILE-15] write onboarding for new mobile developers**

* After ticket is created and you are ready to start solving it, you should move it to **"In progress"** column

## Git Flow

* You should solve tickets in your own branches. When you have a ticket and you are ready to write code, you should create new branch. 

    Branch's name template is `username/ticket_tag`. For example, if you want to to solve ticket named **MOBILE-15** and your username is **danilov6083**, then the branch would be called `danilov6083/MOBILE-15`

* After you wrote some code, you may be want to create a new commit. Commit's message should start with the ticket tag too. For example `MOBILE-15: added onboarding for new developers`

* Finally, when you create new pull request, it's title should start with the ticket tag too. For example `MOBILE-15: added onboarding for new developers`. 

    Also don't forget to add some reviewers to check your code and set "develop branch" as target branch for merging

* After your pull request is merged, you can move your ticket to **Ready for check** column in **Wekan**

* Last but not least - no more than one branch per ticket!

That's it :)
