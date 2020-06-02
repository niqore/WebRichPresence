setInterval(function() {
    VILOS_PLAYERJS.getCurrentTime(function (time) {
        VILOS_PLAYERJS.getDuration(function (duration) {
            VILOS_PLAYERJS.getPaused(function (paused) {
                document.dispatchEvent(new CustomEvent('RW759_connectExtension', {
                    detail: [VILOS_PLAYERJS!=null, time, duration, !paused]
                }));
            });
        });
    });
}, 500);