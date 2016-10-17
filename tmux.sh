tmux new -s my-session -d 'bash'
tmux send-keys -t my-session './gradlew run' C-m
tmux split-window 'bash'
tmux send-keys -t my-session 'cd front-end && nvm use stable && npm run-script run' C-m
tmux a -t my-session
