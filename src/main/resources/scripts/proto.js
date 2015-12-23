OUT = {};

/**
 * Set left chassis speed
 * @param float_max100 - speed from -100 to 100
 */
OUT.chassis_left = function (float_max100) {
    return;
};

/**
 * Set right chassis speed
 * @param float_max100 - speed from -100 to 100
 */
OUT.chassis_right = function (float_max100) {
    return;
};

/**
 * Save data to rx0 (float)
 * @param float_data - data
 */
OUT.rx0 = function (float_data) {
    return;
};

/**
 * Save data to rx1 (float)
 * @param float_data - data
 */
OUT.rx1 = function (float_data) {
    return;
};

/**
 * Save data to dx0 (int)
 * @param int_data - data
 */
OUT.dx0 = function (int_data) {
    return;
};

/**
 * Save data to dx1 (int)
 * @param int_data - data
 */
OUT.dx1 = function (int_data) {
    return;
};

/**
 * Scanner world rotation
 * @param float_angle - data
 */
OUT.scan = function (float_angle) {
    return;
};

/**
 * Shoot
 */
OUT.fire = function () {
    return;
};


IN = {};

/**
 * Robot world X position
 * @returns {float}
 */
IN.x = function () {
    return 0.0;
};

/**
 * Robot world Y position
 * @returns {float}
 */
IN.y = function () {
    return 0.0;
};

/**
 * Robot world rotation
 * @returns {float}
 */
IN.angle = function () {
    return 0.0;
};

/**
 * 100 - just shot
 * 0 - turret is ready
 * @returns {float}
 */
IN.turret_wait = function () {
    return 0.0;
};

/**
 * Armor from 100 to 0
 * @returns {float}
 */
IN.armor = function () {
    return 0.0;
};

/**
 * Distance to scanned object
 * @returns {float}
 */
IN.scan_distance = function () {
    return 0.0;
};

/**
 * Type of scanned object
 * @returns {int}
 */
IN.scan_type = function () {
    return 0;
};

/**
 * Load data from rx0 (float)
 * @returns {float}
 */
IN.rx0 = function () {
    return 0.0;
};

/**
 * Load data from rx1 (float)
 * @returns {float}
 */
IN.rx1 = function () {
    return 0.0;
};

/**
 * Load data from dx0 (int)
 * @returns {float}
 */
IN.dx0 = function () {
    return 0;
};

/**
 * Load data from dx1 (int)
 * @returns {float}
 */
IN.dx1 = function () {
    return 0;
};
