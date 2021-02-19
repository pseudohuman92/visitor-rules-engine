/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.PlayCard = (function() {

    /**
     * Properties of a PlayCard.
     * @exports IPlayCard
     * @interface IPlayCard
     * @property {string|null} [cardID] PlayCard cardID
     */

    /**
     * Constructs a new PlayCard.
     * @exports PlayCard
     * @classdesc Represents a PlayCard.
     * @implements IPlayCard
     * @constructor
     * @param {IPlayCard=} [properties] Properties to set
     */
    function PlayCard(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * PlayCard cardID.
     * @member {string} cardID
     * @memberof PlayCard
     * @instance
     */
    PlayCard.prototype.cardID = "";

    /**
     * Creates a new PlayCard instance using the specified properties.
     * @function create
     * @memberof PlayCard
     * @static
     * @param {IPlayCard=} [properties] Properties to set
     * @returns {PlayCard} PlayCard instance
     */
    PlayCard.create = function create(properties) {
        return new PlayCard(properties);
    };

    /**
     * Encodes the specified PlayCard message. Does not implicitly {@link PlayCard.verify|verify} messages.
     * @function encode
     * @memberof PlayCard
     * @static
     * @param {IPlayCard} message PlayCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PlayCard.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.cardID != null && Object.hasOwnProperty.call(message, "cardID"))
            writer.uint32(/* id 3, wireType 2 =*/26).string(message.cardID);
        return writer;
    };

    /**
     * Encodes the specified PlayCard message, length delimited. Does not implicitly {@link PlayCard.verify|verify} messages.
     * @function encodeDelimited
     * @memberof PlayCard
     * @static
     * @param {IPlayCard} message PlayCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PlayCard.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a PlayCard message from the specified reader or buffer.
     * @function decode
     * @memberof PlayCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {PlayCard} PlayCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PlayCard.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.PlayCard();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 3:
                message.cardID = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a PlayCard message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof PlayCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {PlayCard} PlayCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PlayCard.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a PlayCard message.
     * @function verify
     * @memberof PlayCard
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    PlayCard.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            if (!$util.isString(message.cardID))
                return "cardID: string expected";
        return null;
    };

    /**
     * Creates a PlayCard message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof PlayCard
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {PlayCard} PlayCard
     */
    PlayCard.fromObject = function fromObject(object) {
        if (object instanceof $root.PlayCard)
            return object;
        var message = new $root.PlayCard();
        if (object.cardID != null)
            message.cardID = String(object.cardID);
        return message;
    };

    /**
     * Creates a plain object from a PlayCard message. Also converts values to other types if specified.
     * @function toObject
     * @memberof PlayCard
     * @static
     * @param {PlayCard} message PlayCard
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    PlayCard.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.cardID = "";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            object.cardID = message.cardID;
        return object;
    };

    /**
     * Converts this PlayCard to JSON.
     * @function toJSON
     * @memberof PlayCard
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    PlayCard.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return PlayCard;
})();

$root.ActivateCard = (function() {

    /**
     * Properties of an ActivateCard.
     * @exports IActivateCard
     * @interface IActivateCard
     * @property {string|null} [cardID] ActivateCard cardID
     */

    /**
     * Constructs a new ActivateCard.
     * @exports ActivateCard
     * @classdesc Represents an ActivateCard.
     * @implements IActivateCard
     * @constructor
     * @param {IActivateCard=} [properties] Properties to set
     */
    function ActivateCard(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * ActivateCard cardID.
     * @member {string} cardID
     * @memberof ActivateCard
     * @instance
     */
    ActivateCard.prototype.cardID = "";

    /**
     * Creates a new ActivateCard instance using the specified properties.
     * @function create
     * @memberof ActivateCard
     * @static
     * @param {IActivateCard=} [properties] Properties to set
     * @returns {ActivateCard} ActivateCard instance
     */
    ActivateCard.create = function create(properties) {
        return new ActivateCard(properties);
    };

    /**
     * Encodes the specified ActivateCard message. Does not implicitly {@link ActivateCard.verify|verify} messages.
     * @function encode
     * @memberof ActivateCard
     * @static
     * @param {IActivateCard} message ActivateCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ActivateCard.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.cardID != null && Object.hasOwnProperty.call(message, "cardID"))
            writer.uint32(/* id 3, wireType 2 =*/26).string(message.cardID);
        return writer;
    };

    /**
     * Encodes the specified ActivateCard message, length delimited. Does not implicitly {@link ActivateCard.verify|verify} messages.
     * @function encodeDelimited
     * @memberof ActivateCard
     * @static
     * @param {IActivateCard} message ActivateCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ActivateCard.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an ActivateCard message from the specified reader or buffer.
     * @function decode
     * @memberof ActivateCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {ActivateCard} ActivateCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ActivateCard.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.ActivateCard();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 3:
                message.cardID = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an ActivateCard message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof ActivateCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {ActivateCard} ActivateCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ActivateCard.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an ActivateCard message.
     * @function verify
     * @memberof ActivateCard
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    ActivateCard.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            if (!$util.isString(message.cardID))
                return "cardID: string expected";
        return null;
    };

    /**
     * Creates an ActivateCard message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof ActivateCard
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {ActivateCard} ActivateCard
     */
    ActivateCard.fromObject = function fromObject(object) {
        if (object instanceof $root.ActivateCard)
            return object;
        var message = new $root.ActivateCard();
        if (object.cardID != null)
            message.cardID = String(object.cardID);
        return message;
    };

    /**
     * Creates a plain object from an ActivateCard message. Also converts values to other types if specified.
     * @function toObject
     * @memberof ActivateCard
     * @static
     * @param {ActivateCard} message ActivateCard
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    ActivateCard.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.cardID = "";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            object.cardID = message.cardID;
        return object;
    };

    /**
     * Converts this ActivateCard to JSON.
     * @function toJSON
     * @memberof ActivateCard
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    ActivateCard.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return ActivateCard;
})();

$root.StudyCard = (function() {

    /**
     * Properties of a StudyCard.
     * @exports IStudyCard
     * @interface IStudyCard
     * @property {string|null} [cardID] StudyCard cardID
     */

    /**
     * Constructs a new StudyCard.
     * @exports StudyCard
     * @classdesc Represents a StudyCard.
     * @implements IStudyCard
     * @constructor
     * @param {IStudyCard=} [properties] Properties to set
     */
    function StudyCard(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * StudyCard cardID.
     * @member {string} cardID
     * @memberof StudyCard
     * @instance
     */
    StudyCard.prototype.cardID = "";

    /**
     * Creates a new StudyCard instance using the specified properties.
     * @function create
     * @memberof StudyCard
     * @static
     * @param {IStudyCard=} [properties] Properties to set
     * @returns {StudyCard} StudyCard instance
     */
    StudyCard.create = function create(properties) {
        return new StudyCard(properties);
    };

    /**
     * Encodes the specified StudyCard message. Does not implicitly {@link StudyCard.verify|verify} messages.
     * @function encode
     * @memberof StudyCard
     * @static
     * @param {IStudyCard} message StudyCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    StudyCard.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.cardID != null && Object.hasOwnProperty.call(message, "cardID"))
            writer.uint32(/* id 3, wireType 2 =*/26).string(message.cardID);
        return writer;
    };

    /**
     * Encodes the specified StudyCard message, length delimited. Does not implicitly {@link StudyCard.verify|verify} messages.
     * @function encodeDelimited
     * @memberof StudyCard
     * @static
     * @param {IStudyCard} message StudyCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    StudyCard.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a StudyCard message from the specified reader or buffer.
     * @function decode
     * @memberof StudyCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {StudyCard} StudyCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    StudyCard.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.StudyCard();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 3:
                message.cardID = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a StudyCard message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof StudyCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {StudyCard} StudyCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    StudyCard.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a StudyCard message.
     * @function verify
     * @memberof StudyCard
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    StudyCard.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            if (!$util.isString(message.cardID))
                return "cardID: string expected";
        return null;
    };

    /**
     * Creates a StudyCard message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof StudyCard
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {StudyCard} StudyCard
     */
    StudyCard.fromObject = function fromObject(object) {
        if (object instanceof $root.StudyCard)
            return object;
        var message = new $root.StudyCard();
        if (object.cardID != null)
            message.cardID = String(object.cardID);
        return message;
    };

    /**
     * Creates a plain object from a StudyCard message. Also converts values to other types if specified.
     * @function toObject
     * @memberof StudyCard
     * @static
     * @param {StudyCard} message StudyCard
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    StudyCard.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.cardID = "";
        if (message.cardID != null && message.hasOwnProperty("cardID"))
            object.cardID = message.cardID;
        return object;
    };

    /**
     * Converts this StudyCard to JSON.
     * @function toJSON
     * @memberof StudyCard
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    StudyCard.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return StudyCard;
})();

$root.Pass = (function() {

    /**
     * Properties of a Pass.
     * @exports IPass
     * @interface IPass
     */

    /**
     * Constructs a new Pass.
     * @exports Pass
     * @classdesc Represents a Pass.
     * @implements IPass
     * @constructor
     * @param {IPass=} [properties] Properties to set
     */
    function Pass(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Creates a new Pass instance using the specified properties.
     * @function create
     * @memberof Pass
     * @static
     * @param {IPass=} [properties] Properties to set
     * @returns {Pass} Pass instance
     */
    Pass.create = function create(properties) {
        return new Pass(properties);
    };

    /**
     * Encodes the specified Pass message. Does not implicitly {@link Pass.verify|verify} messages.
     * @function encode
     * @memberof Pass
     * @static
     * @param {IPass} message Pass message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Pass.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        return writer;
    };

    /**
     * Encodes the specified Pass message, length delimited. Does not implicitly {@link Pass.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Pass
     * @static
     * @param {IPass} message Pass message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Pass.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Pass message from the specified reader or buffer.
     * @function decode
     * @memberof Pass
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Pass} Pass
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Pass.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Pass();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Pass message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Pass
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Pass} Pass
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Pass.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Pass message.
     * @function verify
     * @memberof Pass
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Pass.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        return null;
    };

    /**
     * Creates a Pass message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Pass
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Pass} Pass
     */
    Pass.fromObject = function fromObject(object) {
        if (object instanceof $root.Pass)
            return object;
        return new $root.Pass();
    };

    /**
     * Creates a plain object from a Pass message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Pass
     * @static
     * @param {Pass} message Pass
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Pass.toObject = function toObject() {
        return {};
    };

    /**
     * Converts this Pass to JSON.
     * @function toJSON
     * @memberof Pass
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Pass.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Pass;
})();

$root.Redraw = (function() {

    /**
     * Properties of a Redraw.
     * @exports IRedraw
     * @interface IRedraw
     */

    /**
     * Constructs a new Redraw.
     * @exports Redraw
     * @classdesc Represents a Redraw.
     * @implements IRedraw
     * @constructor
     * @param {IRedraw=} [properties] Properties to set
     */
    function Redraw(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Creates a new Redraw instance using the specified properties.
     * @function create
     * @memberof Redraw
     * @static
     * @param {IRedraw=} [properties] Properties to set
     * @returns {Redraw} Redraw instance
     */
    Redraw.create = function create(properties) {
        return new Redraw(properties);
    };

    /**
     * Encodes the specified Redraw message. Does not implicitly {@link Redraw.verify|verify} messages.
     * @function encode
     * @memberof Redraw
     * @static
     * @param {IRedraw} message Redraw message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Redraw.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        return writer;
    };

    /**
     * Encodes the specified Redraw message, length delimited. Does not implicitly {@link Redraw.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Redraw
     * @static
     * @param {IRedraw} message Redraw message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Redraw.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Redraw message from the specified reader or buffer.
     * @function decode
     * @memberof Redraw
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Redraw} Redraw
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Redraw.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Redraw();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Redraw message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Redraw
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Redraw} Redraw
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Redraw.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Redraw message.
     * @function verify
     * @memberof Redraw
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Redraw.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        return null;
    };

    /**
     * Creates a Redraw message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Redraw
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Redraw} Redraw
     */
    Redraw.fromObject = function fromObject(object) {
        if (object instanceof $root.Redraw)
            return object;
        return new $root.Redraw();
    };

    /**
     * Creates a plain object from a Redraw message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Redraw
     * @static
     * @param {Redraw} message Redraw
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Redraw.toObject = function toObject() {
        return {};
    };

    /**
     * Converts this Redraw to JSON.
     * @function toJSON
     * @memberof Redraw
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Redraw.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Redraw;
})();

$root.Keep = (function() {

    /**
     * Properties of a Keep.
     * @exports IKeep
     * @interface IKeep
     */

    /**
     * Constructs a new Keep.
     * @exports Keep
     * @classdesc Represents a Keep.
     * @implements IKeep
     * @constructor
     * @param {IKeep=} [properties] Properties to set
     */
    function Keep(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Creates a new Keep instance using the specified properties.
     * @function create
     * @memberof Keep
     * @static
     * @param {IKeep=} [properties] Properties to set
     * @returns {Keep} Keep instance
     */
    Keep.create = function create(properties) {
        return new Keep(properties);
    };

    /**
     * Encodes the specified Keep message. Does not implicitly {@link Keep.verify|verify} messages.
     * @function encode
     * @memberof Keep
     * @static
     * @param {IKeep} message Keep message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Keep.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        return writer;
    };

    /**
     * Encodes the specified Keep message, length delimited. Does not implicitly {@link Keep.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Keep
     * @static
     * @param {IKeep} message Keep message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Keep.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Keep message from the specified reader or buffer.
     * @function decode
     * @memberof Keep
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Keep} Keep
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Keep.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Keep();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Keep message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Keep
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Keep} Keep
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Keep.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Keep message.
     * @function verify
     * @memberof Keep
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Keep.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        return null;
    };

    /**
     * Creates a Keep message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Keep
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Keep} Keep
     */
    Keep.fromObject = function fromObject(object) {
        if (object instanceof $root.Keep)
            return object;
        return new $root.Keep();
    };

    /**
     * Creates a plain object from a Keep message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Keep
     * @static
     * @param {Keep} message Keep
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Keep.toObject = function toObject() {
        return {};
    };

    /**
     * Converts this Keep to JSON.
     * @function toJSON
     * @memberof Keep
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Keep.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Keep;
})();

$root.Concede = (function() {

    /**
     * Properties of a Concede.
     * @exports IConcede
     * @interface IConcede
     */

    /**
     * Constructs a new Concede.
     * @exports Concede
     * @classdesc Represents a Concede.
     * @implements IConcede
     * @constructor
     * @param {IConcede=} [properties] Properties to set
     */
    function Concede(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Creates a new Concede instance using the specified properties.
     * @function create
     * @memberof Concede
     * @static
     * @param {IConcede=} [properties] Properties to set
     * @returns {Concede} Concede instance
     */
    Concede.create = function create(properties) {
        return new Concede(properties);
    };

    /**
     * Encodes the specified Concede message. Does not implicitly {@link Concede.verify|verify} messages.
     * @function encode
     * @memberof Concede
     * @static
     * @param {IConcede} message Concede message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Concede.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        return writer;
    };

    /**
     * Encodes the specified Concede message, length delimited. Does not implicitly {@link Concede.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Concede
     * @static
     * @param {IConcede} message Concede message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Concede.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Concede message from the specified reader or buffer.
     * @function decode
     * @memberof Concede
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Concede} Concede
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Concede.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Concede();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Concede message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Concede
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Concede} Concede
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Concede.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Concede message.
     * @function verify
     * @memberof Concede
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Concede.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        return null;
    };

    /**
     * Creates a Concede message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Concede
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Concede} Concede
     */
    Concede.fromObject = function fromObject(object) {
        if (object instanceof $root.Concede)
            return object;
        return new $root.Concede();
    };

    /**
     * Creates a plain object from a Concede message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Concede
     * @static
     * @param {Concede} message Concede
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Concede.toObject = function toObject() {
        return {};
    };

    /**
     * Converts this Concede to JSON.
     * @function toJSON
     * @memberof Concede
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Concede.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Concede;
})();

$root.OrderCardsResponse = (function() {

    /**
     * Properties of an OrderCardsResponse.
     * @exports IOrderCardsResponse
     * @interface IOrderCardsResponse
     * @property {Array.<string>|null} [orderedCards] OrderCardsResponse orderedCards
     */

    /**
     * Constructs a new OrderCardsResponse.
     * @exports OrderCardsResponse
     * @classdesc Represents an OrderCardsResponse.
     * @implements IOrderCardsResponse
     * @constructor
     * @param {IOrderCardsResponse=} [properties] Properties to set
     */
    function OrderCardsResponse(properties) {
        this.orderedCards = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * OrderCardsResponse orderedCards.
     * @member {Array.<string>} orderedCards
     * @memberof OrderCardsResponse
     * @instance
     */
    OrderCardsResponse.prototype.orderedCards = $util.emptyArray;

    /**
     * Creates a new OrderCardsResponse instance using the specified properties.
     * @function create
     * @memberof OrderCardsResponse
     * @static
     * @param {IOrderCardsResponse=} [properties] Properties to set
     * @returns {OrderCardsResponse} OrderCardsResponse instance
     */
    OrderCardsResponse.create = function create(properties) {
        return new OrderCardsResponse(properties);
    };

    /**
     * Encodes the specified OrderCardsResponse message. Does not implicitly {@link OrderCardsResponse.verify|verify} messages.
     * @function encode
     * @memberof OrderCardsResponse
     * @static
     * @param {IOrderCardsResponse} message OrderCardsResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    OrderCardsResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.orderedCards != null && message.orderedCards.length)
            for (var i = 0; i < message.orderedCards.length; ++i)
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.orderedCards[i]);
        return writer;
    };

    /**
     * Encodes the specified OrderCardsResponse message, length delimited. Does not implicitly {@link OrderCardsResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof OrderCardsResponse
     * @static
     * @param {IOrderCardsResponse} message OrderCardsResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    OrderCardsResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an OrderCardsResponse message from the specified reader or buffer.
     * @function decode
     * @memberof OrderCardsResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {OrderCardsResponse} OrderCardsResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    OrderCardsResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.OrderCardsResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 2:
                if (!(message.orderedCards && message.orderedCards.length))
                    message.orderedCards = [];
                message.orderedCards.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an OrderCardsResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof OrderCardsResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {OrderCardsResponse} OrderCardsResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    OrderCardsResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an OrderCardsResponse message.
     * @function verify
     * @memberof OrderCardsResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    OrderCardsResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.orderedCards != null && message.hasOwnProperty("orderedCards")) {
            if (!Array.isArray(message.orderedCards))
                return "orderedCards: array expected";
            for (var i = 0; i < message.orderedCards.length; ++i)
                if (!$util.isString(message.orderedCards[i]))
                    return "orderedCards: string[] expected";
        }
        return null;
    };

    /**
     * Creates an OrderCardsResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof OrderCardsResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {OrderCardsResponse} OrderCardsResponse
     */
    OrderCardsResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.OrderCardsResponse)
            return object;
        var message = new $root.OrderCardsResponse();
        if (object.orderedCards) {
            if (!Array.isArray(object.orderedCards))
                throw TypeError(".OrderCardsResponse.orderedCards: array expected");
            message.orderedCards = [];
            for (var i = 0; i < object.orderedCards.length; ++i)
                message.orderedCards[i] = String(object.orderedCards[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from an OrderCardsResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof OrderCardsResponse
     * @static
     * @param {OrderCardsResponse} message OrderCardsResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    OrderCardsResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.orderedCards = [];
        if (message.orderedCards && message.orderedCards.length) {
            object.orderedCards = [];
            for (var j = 0; j < message.orderedCards.length; ++j)
                object.orderedCards[j] = message.orderedCards[j];
        }
        return object;
    };

    /**
     * Converts this OrderCardsResponse to JSON.
     * @function toJSON
     * @memberof OrderCardsResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    OrderCardsResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return OrderCardsResponse;
})();

$root.SelectFromResponse = (function() {

    /**
     * Properties of a SelectFromResponse.
     * @exports ISelectFromResponse
     * @interface ISelectFromResponse
     * @property {SelectFromType|null} [messageType] SelectFromResponse messageType
     * @property {Array.<string>|null} [selected] SelectFromResponse selected
     */

    /**
     * Constructs a new SelectFromResponse.
     * @exports SelectFromResponse
     * @classdesc Represents a SelectFromResponse.
     * @implements ISelectFromResponse
     * @constructor
     * @param {ISelectFromResponse=} [properties] Properties to set
     */
    function SelectFromResponse(properties) {
        this.selected = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectFromResponse messageType.
     * @member {SelectFromType} messageType
     * @memberof SelectFromResponse
     * @instance
     */
    SelectFromResponse.prototype.messageType = 0;

    /**
     * SelectFromResponse selected.
     * @member {Array.<string>} selected
     * @memberof SelectFromResponse
     * @instance
     */
    SelectFromResponse.prototype.selected = $util.emptyArray;

    /**
     * Creates a new SelectFromResponse instance using the specified properties.
     * @function create
     * @memberof SelectFromResponse
     * @static
     * @param {ISelectFromResponse=} [properties] Properties to set
     * @returns {SelectFromResponse} SelectFromResponse instance
     */
    SelectFromResponse.create = function create(properties) {
        return new SelectFromResponse(properties);
    };

    /**
     * Encodes the specified SelectFromResponse message. Does not implicitly {@link SelectFromResponse.verify|verify} messages.
     * @function encode
     * @memberof SelectFromResponse
     * @static
     * @param {ISelectFromResponse} message SelectFromResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectFromResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.messageType != null && Object.hasOwnProperty.call(message, "messageType"))
            writer.uint32(/* id 1, wireType 0 =*/8).int32(message.messageType);
        if (message.selected != null && message.selected.length)
            for (var i = 0; i < message.selected.length; ++i)
                writer.uint32(/* id 3, wireType 2 =*/26).string(message.selected[i]);
        return writer;
    };

    /**
     * Encodes the specified SelectFromResponse message, length delimited. Does not implicitly {@link SelectFromResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectFromResponse
     * @static
     * @param {ISelectFromResponse} message SelectFromResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectFromResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectFromResponse message from the specified reader or buffer.
     * @function decode
     * @memberof SelectFromResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectFromResponse} SelectFromResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectFromResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectFromResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.messageType = reader.int32();
                break;
            case 3:
                if (!(message.selected && message.selected.length))
                    message.selected = [];
                message.selected.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectFromResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectFromResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectFromResponse} SelectFromResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectFromResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectFromResponse message.
     * @function verify
     * @memberof SelectFromResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectFromResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.messageType != null && message.hasOwnProperty("messageType"))
            switch (message.messageType) {
            default:
                return "messageType: enum value expected";
            case 0:
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            }
        if (message.selected != null && message.hasOwnProperty("selected")) {
            if (!Array.isArray(message.selected))
                return "selected: array expected";
            for (var i = 0; i < message.selected.length; ++i)
                if (!$util.isString(message.selected[i]))
                    return "selected: string[] expected";
        }
        return null;
    };

    /**
     * Creates a SelectFromResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectFromResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectFromResponse} SelectFromResponse
     */
    SelectFromResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectFromResponse)
            return object;
        var message = new $root.SelectFromResponse();
        switch (object.messageType) {
        case "NOTYPE":
        case 0:
            message.messageType = 0;
            break;
        case "LIST":
        case 1:
            message.messageType = 1;
            break;
        case "HAND":
        case 3:
            message.messageType = 3;
            break;
        case "PLAY":
        case 4:
            message.messageType = 4;
            break;
        case "DISCARD_PILE":
        case 5:
            message.messageType = 5;
            break;
        case "VOID":
        case 6:
            message.messageType = 6;
            break;
        case "STACK":
        case 7:
            message.messageType = 7;
            break;
        }
        if (object.selected) {
            if (!Array.isArray(object.selected))
                throw TypeError(".SelectFromResponse.selected: array expected");
            message.selected = [];
            for (var i = 0; i < object.selected.length; ++i)
                message.selected[i] = String(object.selected[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectFromResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectFromResponse
     * @static
     * @param {SelectFromResponse} message SelectFromResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectFromResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.selected = [];
        if (options.defaults)
            object.messageType = options.enums === String ? "NOTYPE" : 0;
        if (message.messageType != null && message.hasOwnProperty("messageType"))
            object.messageType = options.enums === String ? $root.SelectFromType[message.messageType] : message.messageType;
        if (message.selected && message.selected.length) {
            object.selected = [];
            for (var j = 0; j < message.selected.length; ++j)
                object.selected[j] = message.selected[j];
        }
        return object;
    };

    /**
     * Converts this SelectFromResponse to JSON.
     * @function toJSON
     * @memberof SelectFromResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectFromResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectFromResponse;
})();

$root.SelectXValueResponse = (function() {

    /**
     * Properties of a SelectXValueResponse.
     * @exports ISelectXValueResponse
     * @interface ISelectXValueResponse
     * @property {number|null} [selectedXValue] SelectXValueResponse selectedXValue
     */

    /**
     * Constructs a new SelectXValueResponse.
     * @exports SelectXValueResponse
     * @classdesc Represents a SelectXValueResponse.
     * @implements ISelectXValueResponse
     * @constructor
     * @param {ISelectXValueResponse=} [properties] Properties to set
     */
    function SelectXValueResponse(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectXValueResponse selectedXValue.
     * @member {number} selectedXValue
     * @memberof SelectXValueResponse
     * @instance
     */
    SelectXValueResponse.prototype.selectedXValue = 0;

    /**
     * Creates a new SelectXValueResponse instance using the specified properties.
     * @function create
     * @memberof SelectXValueResponse
     * @static
     * @param {ISelectXValueResponse=} [properties] Properties to set
     * @returns {SelectXValueResponse} SelectXValueResponse instance
     */
    SelectXValueResponse.create = function create(properties) {
        return new SelectXValueResponse(properties);
    };

    /**
     * Encodes the specified SelectXValueResponse message. Does not implicitly {@link SelectXValueResponse.verify|verify} messages.
     * @function encode
     * @memberof SelectXValueResponse
     * @static
     * @param {ISelectXValueResponse} message SelectXValueResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectXValueResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.selectedXValue != null && Object.hasOwnProperty.call(message, "selectedXValue"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.selectedXValue);
        return writer;
    };

    /**
     * Encodes the specified SelectXValueResponse message, length delimited. Does not implicitly {@link SelectXValueResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectXValueResponse
     * @static
     * @param {ISelectXValueResponse} message SelectXValueResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectXValueResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectXValueResponse message from the specified reader or buffer.
     * @function decode
     * @memberof SelectXValueResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectXValueResponse} SelectXValueResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectXValueResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectXValueResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 2:
                message.selectedXValue = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectXValueResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectXValueResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectXValueResponse} SelectXValueResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectXValueResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectXValueResponse message.
     * @function verify
     * @memberof SelectXValueResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectXValueResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.selectedXValue != null && message.hasOwnProperty("selectedXValue"))
            if (!$util.isInteger(message.selectedXValue))
                return "selectedXValue: integer expected";
        return null;
    };

    /**
     * Creates a SelectXValueResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectXValueResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectXValueResponse} SelectXValueResponse
     */
    SelectXValueResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectXValueResponse)
            return object;
        var message = new $root.SelectXValueResponse();
        if (object.selectedXValue != null)
            message.selectedXValue = object.selectedXValue | 0;
        return message;
    };

    /**
     * Creates a plain object from a SelectXValueResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectXValueResponse
     * @static
     * @param {SelectXValueResponse} message SelectXValueResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectXValueResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.selectedXValue = 0;
        if (message.selectedXValue != null && message.hasOwnProperty("selectedXValue"))
            object.selectedXValue = message.selectedXValue;
        return object;
    };

    /**
     * Converts this SelectXValueResponse to JSON.
     * @function toJSON
     * @memberof SelectXValueResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectXValueResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectXValueResponse;
})();

$root.SelectAttackersResponse = (function() {

    /**
     * Properties of a SelectAttackersResponse.
     * @exports ISelectAttackersResponse
     * @interface ISelectAttackersResponse
     * @property {Array.<IAttackerAssignment>|null} [attackers] SelectAttackersResponse attackers
     */

    /**
     * Constructs a new SelectAttackersResponse.
     * @exports SelectAttackersResponse
     * @classdesc Represents a SelectAttackersResponse.
     * @implements ISelectAttackersResponse
     * @constructor
     * @param {ISelectAttackersResponse=} [properties] Properties to set
     */
    function SelectAttackersResponse(properties) {
        this.attackers = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectAttackersResponse attackers.
     * @member {Array.<IAttackerAssignment>} attackers
     * @memberof SelectAttackersResponse
     * @instance
     */
    SelectAttackersResponse.prototype.attackers = $util.emptyArray;

    /**
     * Creates a new SelectAttackersResponse instance using the specified properties.
     * @function create
     * @memberof SelectAttackersResponse
     * @static
     * @param {ISelectAttackersResponse=} [properties] Properties to set
     * @returns {SelectAttackersResponse} SelectAttackersResponse instance
     */
    SelectAttackersResponse.create = function create(properties) {
        return new SelectAttackersResponse(properties);
    };

    /**
     * Encodes the specified SelectAttackersResponse message. Does not implicitly {@link SelectAttackersResponse.verify|verify} messages.
     * @function encode
     * @memberof SelectAttackersResponse
     * @static
     * @param {ISelectAttackersResponse} message SelectAttackersResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectAttackersResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.attackers != null && message.attackers.length)
            for (var i = 0; i < message.attackers.length; ++i)
                $root.AttackerAssignment.encode(message.attackers[i], writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified SelectAttackersResponse message, length delimited. Does not implicitly {@link SelectAttackersResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectAttackersResponse
     * @static
     * @param {ISelectAttackersResponse} message SelectAttackersResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectAttackersResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectAttackersResponse message from the specified reader or buffer.
     * @function decode
     * @memberof SelectAttackersResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectAttackersResponse} SelectAttackersResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectAttackersResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectAttackersResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                if (!(message.attackers && message.attackers.length))
                    message.attackers = [];
                message.attackers.push($root.AttackerAssignment.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectAttackersResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectAttackersResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectAttackersResponse} SelectAttackersResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectAttackersResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectAttackersResponse message.
     * @function verify
     * @memberof SelectAttackersResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectAttackersResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.attackers != null && message.hasOwnProperty("attackers")) {
            if (!Array.isArray(message.attackers))
                return "attackers: array expected";
            for (var i = 0; i < message.attackers.length; ++i) {
                var error = $root.AttackerAssignment.verify(message.attackers[i]);
                if (error)
                    return "attackers." + error;
            }
        }
        return null;
    };

    /**
     * Creates a SelectAttackersResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectAttackersResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectAttackersResponse} SelectAttackersResponse
     */
    SelectAttackersResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectAttackersResponse)
            return object;
        var message = new $root.SelectAttackersResponse();
        if (object.attackers) {
            if (!Array.isArray(object.attackers))
                throw TypeError(".SelectAttackersResponse.attackers: array expected");
            message.attackers = [];
            for (var i = 0; i < object.attackers.length; ++i) {
                if (typeof object.attackers[i] !== "object")
                    throw TypeError(".SelectAttackersResponse.attackers: object expected");
                message.attackers[i] = $root.AttackerAssignment.fromObject(object.attackers[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectAttackersResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectAttackersResponse
     * @static
     * @param {SelectAttackersResponse} message SelectAttackersResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectAttackersResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.attackers = [];
        if (message.attackers && message.attackers.length) {
            object.attackers = [];
            for (var j = 0; j < message.attackers.length; ++j)
                object.attackers[j] = $root.AttackerAssignment.toObject(message.attackers[j], options);
        }
        return object;
    };

    /**
     * Converts this SelectAttackersResponse to JSON.
     * @function toJSON
     * @memberof SelectAttackersResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectAttackersResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectAttackersResponse;
})();

$root.SelectBlockersResponse = (function() {

    /**
     * Properties of a SelectBlockersResponse.
     * @exports ISelectBlockersResponse
     * @interface ISelectBlockersResponse
     * @property {Array.<IBlockerAssignment>|null} [blockers] SelectBlockersResponse blockers
     */

    /**
     * Constructs a new SelectBlockersResponse.
     * @exports SelectBlockersResponse
     * @classdesc Represents a SelectBlockersResponse.
     * @implements ISelectBlockersResponse
     * @constructor
     * @param {ISelectBlockersResponse=} [properties] Properties to set
     */
    function SelectBlockersResponse(properties) {
        this.blockers = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectBlockersResponse blockers.
     * @member {Array.<IBlockerAssignment>} blockers
     * @memberof SelectBlockersResponse
     * @instance
     */
    SelectBlockersResponse.prototype.blockers = $util.emptyArray;

    /**
     * Creates a new SelectBlockersResponse instance using the specified properties.
     * @function create
     * @memberof SelectBlockersResponse
     * @static
     * @param {ISelectBlockersResponse=} [properties] Properties to set
     * @returns {SelectBlockersResponse} SelectBlockersResponse instance
     */
    SelectBlockersResponse.create = function create(properties) {
        return new SelectBlockersResponse(properties);
    };

    /**
     * Encodes the specified SelectBlockersResponse message. Does not implicitly {@link SelectBlockersResponse.verify|verify} messages.
     * @function encode
     * @memberof SelectBlockersResponse
     * @static
     * @param {ISelectBlockersResponse} message SelectBlockersResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectBlockersResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.blockers != null && message.blockers.length)
            for (var i = 0; i < message.blockers.length; ++i)
                $root.BlockerAssignment.encode(message.blockers[i], writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified SelectBlockersResponse message, length delimited. Does not implicitly {@link SelectBlockersResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectBlockersResponse
     * @static
     * @param {ISelectBlockersResponse} message SelectBlockersResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectBlockersResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectBlockersResponse message from the specified reader or buffer.
     * @function decode
     * @memberof SelectBlockersResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectBlockersResponse} SelectBlockersResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectBlockersResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectBlockersResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                if (!(message.blockers && message.blockers.length))
                    message.blockers = [];
                message.blockers.push($root.BlockerAssignment.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectBlockersResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectBlockersResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectBlockersResponse} SelectBlockersResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectBlockersResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectBlockersResponse message.
     * @function verify
     * @memberof SelectBlockersResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectBlockersResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.blockers != null && message.hasOwnProperty("blockers")) {
            if (!Array.isArray(message.blockers))
                return "blockers: array expected";
            for (var i = 0; i < message.blockers.length; ++i) {
                var error = $root.BlockerAssignment.verify(message.blockers[i]);
                if (error)
                    return "blockers." + error;
            }
        }
        return null;
    };

    /**
     * Creates a SelectBlockersResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectBlockersResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectBlockersResponse} SelectBlockersResponse
     */
    SelectBlockersResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectBlockersResponse)
            return object;
        var message = new $root.SelectBlockersResponse();
        if (object.blockers) {
            if (!Array.isArray(object.blockers))
                throw TypeError(".SelectBlockersResponse.blockers: array expected");
            message.blockers = [];
            for (var i = 0; i < object.blockers.length; ++i) {
                if (typeof object.blockers[i] !== "object")
                    throw TypeError(".SelectBlockersResponse.blockers: object expected");
                message.blockers[i] = $root.BlockerAssignment.fromObject(object.blockers[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectBlockersResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectBlockersResponse
     * @static
     * @param {SelectBlockersResponse} message SelectBlockersResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectBlockersResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.blockers = [];
        if (message.blockers && message.blockers.length) {
            object.blockers = [];
            for (var j = 0; j < message.blockers.length; ++j)
                object.blockers[j] = $root.BlockerAssignment.toObject(message.blockers[j], options);
        }
        return object;
    };

    /**
     * Converts this SelectBlockersResponse to JSON.
     * @function toJSON
     * @memberof SelectBlockersResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectBlockersResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectBlockersResponse;
})();

$root.AssignDamageResponse = (function() {

    /**
     * Properties of an AssignDamageResponse.
     * @exports IAssignDamageResponse
     * @interface IAssignDamageResponse
     * @property {Array.<IDamageAssignment>|null} [damageAssignments] AssignDamageResponse damageAssignments
     */

    /**
     * Constructs a new AssignDamageResponse.
     * @exports AssignDamageResponse
     * @classdesc Represents an AssignDamageResponse.
     * @implements IAssignDamageResponse
     * @constructor
     * @param {IAssignDamageResponse=} [properties] Properties to set
     */
    function AssignDamageResponse(properties) {
        this.damageAssignments = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * AssignDamageResponse damageAssignments.
     * @member {Array.<IDamageAssignment>} damageAssignments
     * @memberof AssignDamageResponse
     * @instance
     */
    AssignDamageResponse.prototype.damageAssignments = $util.emptyArray;

    /**
     * Creates a new AssignDamageResponse instance using the specified properties.
     * @function create
     * @memberof AssignDamageResponse
     * @static
     * @param {IAssignDamageResponse=} [properties] Properties to set
     * @returns {AssignDamageResponse} AssignDamageResponse instance
     */
    AssignDamageResponse.create = function create(properties) {
        return new AssignDamageResponse(properties);
    };

    /**
     * Encodes the specified AssignDamageResponse message. Does not implicitly {@link AssignDamageResponse.verify|verify} messages.
     * @function encode
     * @memberof AssignDamageResponse
     * @static
     * @param {IAssignDamageResponse} message AssignDamageResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AssignDamageResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.damageAssignments != null && message.damageAssignments.length)
            for (var i = 0; i < message.damageAssignments.length; ++i)
                $root.DamageAssignment.encode(message.damageAssignments[i], writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified AssignDamageResponse message, length delimited. Does not implicitly {@link AssignDamageResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof AssignDamageResponse
     * @static
     * @param {IAssignDamageResponse} message AssignDamageResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AssignDamageResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an AssignDamageResponse message from the specified reader or buffer.
     * @function decode
     * @memberof AssignDamageResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {AssignDamageResponse} AssignDamageResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AssignDamageResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.AssignDamageResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                if (!(message.damageAssignments && message.damageAssignments.length))
                    message.damageAssignments = [];
                message.damageAssignments.push($root.DamageAssignment.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an AssignDamageResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof AssignDamageResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {AssignDamageResponse} AssignDamageResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AssignDamageResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an AssignDamageResponse message.
     * @function verify
     * @memberof AssignDamageResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    AssignDamageResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.damageAssignments != null && message.hasOwnProperty("damageAssignments")) {
            if (!Array.isArray(message.damageAssignments))
                return "damageAssignments: array expected";
            for (var i = 0; i < message.damageAssignments.length; ++i) {
                var error = $root.DamageAssignment.verify(message.damageAssignments[i]);
                if (error)
                    return "damageAssignments." + error;
            }
        }
        return null;
    };

    /**
     * Creates an AssignDamageResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof AssignDamageResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {AssignDamageResponse} AssignDamageResponse
     */
    AssignDamageResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.AssignDamageResponse)
            return object;
        var message = new $root.AssignDamageResponse();
        if (object.damageAssignments) {
            if (!Array.isArray(object.damageAssignments))
                throw TypeError(".AssignDamageResponse.damageAssignments: array expected");
            message.damageAssignments = [];
            for (var i = 0; i < object.damageAssignments.length; ++i) {
                if (typeof object.damageAssignments[i] !== "object")
                    throw TypeError(".AssignDamageResponse.damageAssignments: object expected");
                message.damageAssignments[i] = $root.DamageAssignment.fromObject(object.damageAssignments[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from an AssignDamageResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof AssignDamageResponse
     * @static
     * @param {AssignDamageResponse} message AssignDamageResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    AssignDamageResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.damageAssignments = [];
        if (message.damageAssignments && message.damageAssignments.length) {
            object.damageAssignments = [];
            for (var j = 0; j < message.damageAssignments.length; ++j)
                object.damageAssignments[j] = $root.DamageAssignment.toObject(message.damageAssignments[j], options);
        }
        return object;
    };

    /**
     * Converts this AssignDamageResponse to JSON.
     * @function toJSON
     * @memberof AssignDamageResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    AssignDamageResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return AssignDamageResponse;
})();

$root.SelectKnowledgeResponse = (function() {

    /**
     * Properties of a SelectKnowledgeResponse.
     * @exports ISelectKnowledgeResponse
     * @interface ISelectKnowledgeResponse
     * @property {Knowledge|null} [selectedKnowledge] SelectKnowledgeResponse selectedKnowledge
     */

    /**
     * Constructs a new SelectKnowledgeResponse.
     * @exports SelectKnowledgeResponse
     * @classdesc Represents a SelectKnowledgeResponse.
     * @implements ISelectKnowledgeResponse
     * @constructor
     * @param {ISelectKnowledgeResponse=} [properties] Properties to set
     */
    function SelectKnowledgeResponse(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectKnowledgeResponse selectedKnowledge.
     * @member {Knowledge} selectedKnowledge
     * @memberof SelectKnowledgeResponse
     * @instance
     */
    SelectKnowledgeResponse.prototype.selectedKnowledge = 0;

    /**
     * Creates a new SelectKnowledgeResponse instance using the specified properties.
     * @function create
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {ISelectKnowledgeResponse=} [properties] Properties to set
     * @returns {SelectKnowledgeResponse} SelectKnowledgeResponse instance
     */
    SelectKnowledgeResponse.create = function create(properties) {
        return new SelectKnowledgeResponse(properties);
    };

    /**
     * Encodes the specified SelectKnowledgeResponse message. Does not implicitly {@link SelectKnowledgeResponse.verify|verify} messages.
     * @function encode
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {ISelectKnowledgeResponse} message SelectKnowledgeResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectKnowledgeResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.selectedKnowledge != null && Object.hasOwnProperty.call(message, "selectedKnowledge"))
            writer.uint32(/* id 1, wireType 0 =*/8).int32(message.selectedKnowledge);
        return writer;
    };

    /**
     * Encodes the specified SelectKnowledgeResponse message, length delimited. Does not implicitly {@link SelectKnowledgeResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {ISelectKnowledgeResponse} message SelectKnowledgeResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectKnowledgeResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectKnowledgeResponse message from the specified reader or buffer.
     * @function decode
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectKnowledgeResponse} SelectKnowledgeResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectKnowledgeResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectKnowledgeResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.selectedKnowledge = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectKnowledgeResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectKnowledgeResponse} SelectKnowledgeResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectKnowledgeResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectKnowledgeResponse message.
     * @function verify
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectKnowledgeResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.selectedKnowledge != null && message.hasOwnProperty("selectedKnowledge"))
            switch (message.selectedKnowledge) {
            default:
                return "selectedKnowledge: enum value expected";
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            }
        return null;
    };

    /**
     * Creates a SelectKnowledgeResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectKnowledgeResponse} SelectKnowledgeResponse
     */
    SelectKnowledgeResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectKnowledgeResponse)
            return object;
        var message = new $root.SelectKnowledgeResponse();
        switch (object.selectedKnowledge) {
        case "NONE":
        case 0:
            message.selectedKnowledge = 0;
            break;
        case "PURPLE":
        case 1:
            message.selectedKnowledge = 1;
            break;
        case "GREEN":
        case 2:
            message.selectedKnowledge = 2;
            break;
        case "RED":
        case 3:
            message.selectedKnowledge = 3;
            break;
        case "BLUE":
        case 4:
            message.selectedKnowledge = 4;
            break;
        case "YELLOW":
        case 5:
            message.selectedKnowledge = 5;
            break;
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectKnowledgeResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectKnowledgeResponse
     * @static
     * @param {SelectKnowledgeResponse} message SelectKnowledgeResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectKnowledgeResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.selectedKnowledge = options.enums === String ? "NONE" : 0;
        if (message.selectedKnowledge != null && message.hasOwnProperty("selectedKnowledge"))
            object.selectedKnowledge = options.enums === String ? $root.Knowledge[message.selectedKnowledge] : message.selectedKnowledge;
        return object;
    };

    /**
     * Converts this SelectKnowledgeResponse to JSON.
     * @function toJSON
     * @memberof SelectKnowledgeResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectKnowledgeResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectKnowledgeResponse;
})();

$root.PickCardResponse = (function() {

    /**
     * Properties of a PickCardResponse.
     * @exports IPickCardResponse
     * @interface IPickCardResponse
     * @property {string|null} [pickedCard] PickCardResponse pickedCard
     */

    /**
     * Constructs a new PickCardResponse.
     * @exports PickCardResponse
     * @classdesc Represents a PickCardResponse.
     * @implements IPickCardResponse
     * @constructor
     * @param {IPickCardResponse=} [properties] Properties to set
     */
    function PickCardResponse(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * PickCardResponse pickedCard.
     * @member {string} pickedCard
     * @memberof PickCardResponse
     * @instance
     */
    PickCardResponse.prototype.pickedCard = "";

    /**
     * Creates a new PickCardResponse instance using the specified properties.
     * @function create
     * @memberof PickCardResponse
     * @static
     * @param {IPickCardResponse=} [properties] Properties to set
     * @returns {PickCardResponse} PickCardResponse instance
     */
    PickCardResponse.create = function create(properties) {
        return new PickCardResponse(properties);
    };

    /**
     * Encodes the specified PickCardResponse message. Does not implicitly {@link PickCardResponse.verify|verify} messages.
     * @function encode
     * @memberof PickCardResponse
     * @static
     * @param {IPickCardResponse} message PickCardResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PickCardResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.pickedCard != null && Object.hasOwnProperty.call(message, "pickedCard"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.pickedCard);
        return writer;
    };

    /**
     * Encodes the specified PickCardResponse message, length delimited. Does not implicitly {@link PickCardResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof PickCardResponse
     * @static
     * @param {IPickCardResponse} message PickCardResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PickCardResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a PickCardResponse message from the specified reader or buffer.
     * @function decode
     * @memberof PickCardResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {PickCardResponse} PickCardResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PickCardResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.PickCardResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.pickedCard = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a PickCardResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof PickCardResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {PickCardResponse} PickCardResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PickCardResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a PickCardResponse message.
     * @function verify
     * @memberof PickCardResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    PickCardResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.pickedCard != null && message.hasOwnProperty("pickedCard"))
            if (!$util.isString(message.pickedCard))
                return "pickedCard: string expected";
        return null;
    };

    /**
     * Creates a PickCardResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof PickCardResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {PickCardResponse} PickCardResponse
     */
    PickCardResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.PickCardResponse)
            return object;
        var message = new $root.PickCardResponse();
        if (object.pickedCard != null)
            message.pickedCard = String(object.pickedCard);
        return message;
    };

    /**
     * Creates a plain object from a PickCardResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof PickCardResponse
     * @static
     * @param {PickCardResponse} message PickCardResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    PickCardResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.pickedCard = "";
        if (message.pickedCard != null && message.hasOwnProperty("pickedCard"))
            object.pickedCard = message.pickedCard;
        return object;
    };

    /**
     * Converts this PickCardResponse to JSON.
     * @function toJSON
     * @memberof PickCardResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    PickCardResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return PickCardResponse;
})();

$root.SaveGameState = (function() {

    /**
     * Properties of a SaveGameState.
     * @exports ISaveGameState
     * @interface ISaveGameState
     * @property {string|null} [filename] SaveGameState filename
     */

    /**
     * Constructs a new SaveGameState.
     * @exports SaveGameState
     * @classdesc Represents a SaveGameState.
     * @implements ISaveGameState
     * @constructor
     * @param {ISaveGameState=} [properties] Properties to set
     */
    function SaveGameState(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SaveGameState filename.
     * @member {string} filename
     * @memberof SaveGameState
     * @instance
     */
    SaveGameState.prototype.filename = "";

    /**
     * Creates a new SaveGameState instance using the specified properties.
     * @function create
     * @memberof SaveGameState
     * @static
     * @param {ISaveGameState=} [properties] Properties to set
     * @returns {SaveGameState} SaveGameState instance
     */
    SaveGameState.create = function create(properties) {
        return new SaveGameState(properties);
    };

    /**
     * Encodes the specified SaveGameState message. Does not implicitly {@link SaveGameState.verify|verify} messages.
     * @function encode
     * @memberof SaveGameState
     * @static
     * @param {ISaveGameState} message SaveGameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SaveGameState.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.filename != null && Object.hasOwnProperty.call(message, "filename"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.filename);
        return writer;
    };

    /**
     * Encodes the specified SaveGameState message, length delimited. Does not implicitly {@link SaveGameState.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SaveGameState
     * @static
     * @param {ISaveGameState} message SaveGameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SaveGameState.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SaveGameState message from the specified reader or buffer.
     * @function decode
     * @memberof SaveGameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SaveGameState} SaveGameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SaveGameState.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SaveGameState();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.filename = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SaveGameState message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SaveGameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SaveGameState} SaveGameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SaveGameState.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SaveGameState message.
     * @function verify
     * @memberof SaveGameState
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SaveGameState.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.filename != null && message.hasOwnProperty("filename"))
            if (!$util.isString(message.filename))
                return "filename: string expected";
        return null;
    };

    /**
     * Creates a SaveGameState message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SaveGameState
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SaveGameState} SaveGameState
     */
    SaveGameState.fromObject = function fromObject(object) {
        if (object instanceof $root.SaveGameState)
            return object;
        var message = new $root.SaveGameState();
        if (object.filename != null)
            message.filename = String(object.filename);
        return message;
    };

    /**
     * Creates a plain object from a SaveGameState message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SaveGameState
     * @static
     * @param {SaveGameState} message SaveGameState
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SaveGameState.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.filename = "";
        if (message.filename != null && message.hasOwnProperty("filename"))
            object.filename = message.filename;
        return object;
    };

    /**
     * Converts this SaveGameState to JSON.
     * @function toJSON
     * @memberof SaveGameState
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SaveGameState.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SaveGameState;
})();

$root.ClientGameMessage = (function() {

    /**
     * Properties of a ClientGameMessage.
     * @exports IClientGameMessage
     * @interface IClientGameMessage
     * @property {IPlayCard|null} [playCard] ClientGameMessage playCard
     * @property {IActivateCard|null} [activateCard] ClientGameMessage activateCard
     * @property {IStudyCard|null} [studyCard] ClientGameMessage studyCard
     * @property {IPass|null} [pass] ClientGameMessage pass
     * @property {IRedraw|null} [redraw] ClientGameMessage redraw
     * @property {IKeep|null} [keep] ClientGameMessage keep
     * @property {IConcede|null} [concede] ClientGameMessage concede
     * @property {IOrderCardsResponse|null} [orderCardsResponse] ClientGameMessage orderCardsResponse
     * @property {ISelectFromResponse|null} [selectFromResponse] ClientGameMessage selectFromResponse
     * @property {ISelectXValueResponse|null} [selectXValueResponse] ClientGameMessage selectXValueResponse
     * @property {ISelectAttackersResponse|null} [selectAttackersResponse] ClientGameMessage selectAttackersResponse
     * @property {ISelectBlockersResponse|null} [selectBlockersResponse] ClientGameMessage selectBlockersResponse
     * @property {ISaveGameState|null} [saveGameState] ClientGameMessage saveGameState
     * @property {IAssignDamageResponse|null} [assignDamageResponse] ClientGameMessage assignDamageResponse
     * @property {ISelectKnowledgeResponse|null} [selectKnowledgeResponse] ClientGameMessage selectKnowledgeResponse
     * @property {IPickCardResponse|null} [pickCardResponse] ClientGameMessage pickCardResponse
     */

    /**
     * Constructs a new ClientGameMessage.
     * @exports ClientGameMessage
     * @classdesc Represents a ClientGameMessage.
     * @implements IClientGameMessage
     * @constructor
     * @param {IClientGameMessage=} [properties] Properties to set
     */
    function ClientGameMessage(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * ClientGameMessage playCard.
     * @member {IPlayCard|null|undefined} playCard
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.playCard = null;

    /**
     * ClientGameMessage activateCard.
     * @member {IActivateCard|null|undefined} activateCard
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.activateCard = null;

    /**
     * ClientGameMessage studyCard.
     * @member {IStudyCard|null|undefined} studyCard
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.studyCard = null;

    /**
     * ClientGameMessage pass.
     * @member {IPass|null|undefined} pass
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.pass = null;

    /**
     * ClientGameMessage redraw.
     * @member {IRedraw|null|undefined} redraw
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.redraw = null;

    /**
     * ClientGameMessage keep.
     * @member {IKeep|null|undefined} keep
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.keep = null;

    /**
     * ClientGameMessage concede.
     * @member {IConcede|null|undefined} concede
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.concede = null;

    /**
     * ClientGameMessage orderCardsResponse.
     * @member {IOrderCardsResponse|null|undefined} orderCardsResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.orderCardsResponse = null;

    /**
     * ClientGameMessage selectFromResponse.
     * @member {ISelectFromResponse|null|undefined} selectFromResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.selectFromResponse = null;

    /**
     * ClientGameMessage selectXValueResponse.
     * @member {ISelectXValueResponse|null|undefined} selectXValueResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.selectXValueResponse = null;

    /**
     * ClientGameMessage selectAttackersResponse.
     * @member {ISelectAttackersResponse|null|undefined} selectAttackersResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.selectAttackersResponse = null;

    /**
     * ClientGameMessage selectBlockersResponse.
     * @member {ISelectBlockersResponse|null|undefined} selectBlockersResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.selectBlockersResponse = null;

    /**
     * ClientGameMessage saveGameState.
     * @member {ISaveGameState|null|undefined} saveGameState
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.saveGameState = null;

    /**
     * ClientGameMessage assignDamageResponse.
     * @member {IAssignDamageResponse|null|undefined} assignDamageResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.assignDamageResponse = null;

    /**
     * ClientGameMessage selectKnowledgeResponse.
     * @member {ISelectKnowledgeResponse|null|undefined} selectKnowledgeResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.selectKnowledgeResponse = null;

    /**
     * ClientGameMessage pickCardResponse.
     * @member {IPickCardResponse|null|undefined} pickCardResponse
     * @memberof ClientGameMessage
     * @instance
     */
    ClientGameMessage.prototype.pickCardResponse = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * ClientGameMessage payload.
     * @member {"playCard"|"activateCard"|"studyCard"|"pass"|"redraw"|"keep"|"concede"|"orderCardsResponse"|"selectFromResponse"|"selectXValueResponse"|"selectAttackersResponse"|"selectBlockersResponse"|"saveGameState"|"assignDamageResponse"|"selectKnowledgeResponse"|"pickCardResponse"|undefined} payload
     * @memberof ClientGameMessage
     * @instance
     */
    Object.defineProperty(ClientGameMessage.prototype, "payload", {
        get: $util.oneOfGetter($oneOfFields = ["playCard", "activateCard", "studyCard", "pass", "redraw", "keep", "concede", "orderCardsResponse", "selectFromResponse", "selectXValueResponse", "selectAttackersResponse", "selectBlockersResponse", "saveGameState", "assignDamageResponse", "selectKnowledgeResponse", "pickCardResponse"]),
        set: $util.oneOfSetter($oneOfFields)
    });

    /**
     * Creates a new ClientGameMessage instance using the specified properties.
     * @function create
     * @memberof ClientGameMessage
     * @static
     * @param {IClientGameMessage=} [properties] Properties to set
     * @returns {ClientGameMessage} ClientGameMessage instance
     */
    ClientGameMessage.create = function create(properties) {
        return new ClientGameMessage(properties);
    };

    /**
     * Encodes the specified ClientGameMessage message. Does not implicitly {@link ClientGameMessage.verify|verify} messages.
     * @function encode
     * @memberof ClientGameMessage
     * @static
     * @param {IClientGameMessage} message ClientGameMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ClientGameMessage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.playCard != null && Object.hasOwnProperty.call(message, "playCard"))
            $root.PlayCard.encode(message.playCard, writer.uint32(/* id 7, wireType 2 =*/58).fork()).ldelim();
        if (message.activateCard != null && Object.hasOwnProperty.call(message, "activateCard"))
            $root.ActivateCard.encode(message.activateCard, writer.uint32(/* id 8, wireType 2 =*/66).fork()).ldelim();
        if (message.studyCard != null && Object.hasOwnProperty.call(message, "studyCard"))
            $root.StudyCard.encode(message.studyCard, writer.uint32(/* id 9, wireType 2 =*/74).fork()).ldelim();
        if (message.pass != null && Object.hasOwnProperty.call(message, "pass"))
            $root.Pass.encode(message.pass, writer.uint32(/* id 10, wireType 2 =*/82).fork()).ldelim();
        if (message.redraw != null && Object.hasOwnProperty.call(message, "redraw"))
            $root.Redraw.encode(message.redraw, writer.uint32(/* id 13, wireType 2 =*/106).fork()).ldelim();
        if (message.keep != null && Object.hasOwnProperty.call(message, "keep"))
            $root.Keep.encode(message.keep, writer.uint32(/* id 14, wireType 2 =*/114).fork()).ldelim();
        if (message.selectFromResponse != null && Object.hasOwnProperty.call(message, "selectFromResponse"))
            $root.SelectFromResponse.encode(message.selectFromResponse, writer.uint32(/* id 15, wireType 2 =*/122).fork()).ldelim();
        if (message.concede != null && Object.hasOwnProperty.call(message, "concede"))
            $root.Concede.encode(message.concede, writer.uint32(/* id 16, wireType 2 =*/130).fork()).ldelim();
        if (message.orderCardsResponse != null && Object.hasOwnProperty.call(message, "orderCardsResponse"))
            $root.OrderCardsResponse.encode(message.orderCardsResponse, writer.uint32(/* id 17, wireType 2 =*/138).fork()).ldelim();
        if (message.selectXValueResponse != null && Object.hasOwnProperty.call(message, "selectXValueResponse"))
            $root.SelectXValueResponse.encode(message.selectXValueResponse, writer.uint32(/* id 23, wireType 2 =*/186).fork()).ldelim();
        if (message.selectAttackersResponse != null && Object.hasOwnProperty.call(message, "selectAttackersResponse"))
            $root.SelectAttackersResponse.encode(message.selectAttackersResponse, writer.uint32(/* id 24, wireType 2 =*/194).fork()).ldelim();
        if (message.selectBlockersResponse != null && Object.hasOwnProperty.call(message, "selectBlockersResponse"))
            $root.SelectBlockersResponse.encode(message.selectBlockersResponse, writer.uint32(/* id 25, wireType 2 =*/202).fork()).ldelim();
        if (message.saveGameState != null && Object.hasOwnProperty.call(message, "saveGameState"))
            $root.SaveGameState.encode(message.saveGameState, writer.uint32(/* id 26, wireType 2 =*/210).fork()).ldelim();
        if (message.assignDamageResponse != null && Object.hasOwnProperty.call(message, "assignDamageResponse"))
            $root.AssignDamageResponse.encode(message.assignDamageResponse, writer.uint32(/* id 27, wireType 2 =*/218).fork()).ldelim();
        if (message.selectKnowledgeResponse != null && Object.hasOwnProperty.call(message, "selectKnowledgeResponse"))
            $root.SelectKnowledgeResponse.encode(message.selectKnowledgeResponse, writer.uint32(/* id 28, wireType 2 =*/226).fork()).ldelim();
        if (message.pickCardResponse != null && Object.hasOwnProperty.call(message, "pickCardResponse"))
            $root.PickCardResponse.encode(message.pickCardResponse, writer.uint32(/* id 29, wireType 2 =*/234).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified ClientGameMessage message, length delimited. Does not implicitly {@link ClientGameMessage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof ClientGameMessage
     * @static
     * @param {IClientGameMessage} message ClientGameMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ClientGameMessage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a ClientGameMessage message from the specified reader or buffer.
     * @function decode
     * @memberof ClientGameMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {ClientGameMessage} ClientGameMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ClientGameMessage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.ClientGameMessage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 7:
                message.playCard = $root.PlayCard.decode(reader, reader.uint32());
                break;
            case 8:
                message.activateCard = $root.ActivateCard.decode(reader, reader.uint32());
                break;
            case 9:
                message.studyCard = $root.StudyCard.decode(reader, reader.uint32());
                break;
            case 10:
                message.pass = $root.Pass.decode(reader, reader.uint32());
                break;
            case 13:
                message.redraw = $root.Redraw.decode(reader, reader.uint32());
                break;
            case 14:
                message.keep = $root.Keep.decode(reader, reader.uint32());
                break;
            case 16:
                message.concede = $root.Concede.decode(reader, reader.uint32());
                break;
            case 17:
                message.orderCardsResponse = $root.OrderCardsResponse.decode(reader, reader.uint32());
                break;
            case 15:
                message.selectFromResponse = $root.SelectFromResponse.decode(reader, reader.uint32());
                break;
            case 23:
                message.selectXValueResponse = $root.SelectXValueResponse.decode(reader, reader.uint32());
                break;
            case 24:
                message.selectAttackersResponse = $root.SelectAttackersResponse.decode(reader, reader.uint32());
                break;
            case 25:
                message.selectBlockersResponse = $root.SelectBlockersResponse.decode(reader, reader.uint32());
                break;
            case 26:
                message.saveGameState = $root.SaveGameState.decode(reader, reader.uint32());
                break;
            case 27:
                message.assignDamageResponse = $root.AssignDamageResponse.decode(reader, reader.uint32());
                break;
            case 28:
                message.selectKnowledgeResponse = $root.SelectKnowledgeResponse.decode(reader, reader.uint32());
                break;
            case 29:
                message.pickCardResponse = $root.PickCardResponse.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a ClientGameMessage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof ClientGameMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {ClientGameMessage} ClientGameMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ClientGameMessage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a ClientGameMessage message.
     * @function verify
     * @memberof ClientGameMessage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    ClientGameMessage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        var properties = {};
        if (message.playCard != null && message.hasOwnProperty("playCard")) {
            properties.payload = 1;
            {
                var error = $root.PlayCard.verify(message.playCard);
                if (error)
                    return "playCard." + error;
            }
        }
        if (message.activateCard != null && message.hasOwnProperty("activateCard")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.ActivateCard.verify(message.activateCard);
                if (error)
                    return "activateCard." + error;
            }
        }
        if (message.studyCard != null && message.hasOwnProperty("studyCard")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.StudyCard.verify(message.studyCard);
                if (error)
                    return "studyCard." + error;
            }
        }
        if (message.pass != null && message.hasOwnProperty("pass")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.Pass.verify(message.pass);
                if (error)
                    return "pass." + error;
            }
        }
        if (message.redraw != null && message.hasOwnProperty("redraw")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.Redraw.verify(message.redraw);
                if (error)
                    return "redraw." + error;
            }
        }
        if (message.keep != null && message.hasOwnProperty("keep")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.Keep.verify(message.keep);
                if (error)
                    return "keep." + error;
            }
        }
        if (message.concede != null && message.hasOwnProperty("concede")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.Concede.verify(message.concede);
                if (error)
                    return "concede." + error;
            }
        }
        if (message.orderCardsResponse != null && message.hasOwnProperty("orderCardsResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.OrderCardsResponse.verify(message.orderCardsResponse);
                if (error)
                    return "orderCardsResponse." + error;
            }
        }
        if (message.selectFromResponse != null && message.hasOwnProperty("selectFromResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectFromResponse.verify(message.selectFromResponse);
                if (error)
                    return "selectFromResponse." + error;
            }
        }
        if (message.selectXValueResponse != null && message.hasOwnProperty("selectXValueResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectXValueResponse.verify(message.selectXValueResponse);
                if (error)
                    return "selectXValueResponse." + error;
            }
        }
        if (message.selectAttackersResponse != null && message.hasOwnProperty("selectAttackersResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectAttackersResponse.verify(message.selectAttackersResponse);
                if (error)
                    return "selectAttackersResponse." + error;
            }
        }
        if (message.selectBlockersResponse != null && message.hasOwnProperty("selectBlockersResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectBlockersResponse.verify(message.selectBlockersResponse);
                if (error)
                    return "selectBlockersResponse." + error;
            }
        }
        if (message.saveGameState != null && message.hasOwnProperty("saveGameState")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SaveGameState.verify(message.saveGameState);
                if (error)
                    return "saveGameState." + error;
            }
        }
        if (message.assignDamageResponse != null && message.hasOwnProperty("assignDamageResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.AssignDamageResponse.verify(message.assignDamageResponse);
                if (error)
                    return "assignDamageResponse." + error;
            }
        }
        if (message.selectKnowledgeResponse != null && message.hasOwnProperty("selectKnowledgeResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectKnowledgeResponse.verify(message.selectKnowledgeResponse);
                if (error)
                    return "selectKnowledgeResponse." + error;
            }
        }
        if (message.pickCardResponse != null && message.hasOwnProperty("pickCardResponse")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.PickCardResponse.verify(message.pickCardResponse);
                if (error)
                    return "pickCardResponse." + error;
            }
        }
        return null;
    };

    /**
     * Creates a ClientGameMessage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof ClientGameMessage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {ClientGameMessage} ClientGameMessage
     */
    ClientGameMessage.fromObject = function fromObject(object) {
        if (object instanceof $root.ClientGameMessage)
            return object;
        var message = new $root.ClientGameMessage();
        if (object.playCard != null) {
            if (typeof object.playCard !== "object")
                throw TypeError(".ClientGameMessage.playCard: object expected");
            message.playCard = $root.PlayCard.fromObject(object.playCard);
        }
        if (object.activateCard != null) {
            if (typeof object.activateCard !== "object")
                throw TypeError(".ClientGameMessage.activateCard: object expected");
            message.activateCard = $root.ActivateCard.fromObject(object.activateCard);
        }
        if (object.studyCard != null) {
            if (typeof object.studyCard !== "object")
                throw TypeError(".ClientGameMessage.studyCard: object expected");
            message.studyCard = $root.StudyCard.fromObject(object.studyCard);
        }
        if (object.pass != null) {
            if (typeof object.pass !== "object")
                throw TypeError(".ClientGameMessage.pass: object expected");
            message.pass = $root.Pass.fromObject(object.pass);
        }
        if (object.redraw != null) {
            if (typeof object.redraw !== "object")
                throw TypeError(".ClientGameMessage.redraw: object expected");
            message.redraw = $root.Redraw.fromObject(object.redraw);
        }
        if (object.keep != null) {
            if (typeof object.keep !== "object")
                throw TypeError(".ClientGameMessage.keep: object expected");
            message.keep = $root.Keep.fromObject(object.keep);
        }
        if (object.concede != null) {
            if (typeof object.concede !== "object")
                throw TypeError(".ClientGameMessage.concede: object expected");
            message.concede = $root.Concede.fromObject(object.concede);
        }
        if (object.orderCardsResponse != null) {
            if (typeof object.orderCardsResponse !== "object")
                throw TypeError(".ClientGameMessage.orderCardsResponse: object expected");
            message.orderCardsResponse = $root.OrderCardsResponse.fromObject(object.orderCardsResponse);
        }
        if (object.selectFromResponse != null) {
            if (typeof object.selectFromResponse !== "object")
                throw TypeError(".ClientGameMessage.selectFromResponse: object expected");
            message.selectFromResponse = $root.SelectFromResponse.fromObject(object.selectFromResponse);
        }
        if (object.selectXValueResponse != null) {
            if (typeof object.selectXValueResponse !== "object")
                throw TypeError(".ClientGameMessage.selectXValueResponse: object expected");
            message.selectXValueResponse = $root.SelectXValueResponse.fromObject(object.selectXValueResponse);
        }
        if (object.selectAttackersResponse != null) {
            if (typeof object.selectAttackersResponse !== "object")
                throw TypeError(".ClientGameMessage.selectAttackersResponse: object expected");
            message.selectAttackersResponse = $root.SelectAttackersResponse.fromObject(object.selectAttackersResponse);
        }
        if (object.selectBlockersResponse != null) {
            if (typeof object.selectBlockersResponse !== "object")
                throw TypeError(".ClientGameMessage.selectBlockersResponse: object expected");
            message.selectBlockersResponse = $root.SelectBlockersResponse.fromObject(object.selectBlockersResponse);
        }
        if (object.saveGameState != null) {
            if (typeof object.saveGameState !== "object")
                throw TypeError(".ClientGameMessage.saveGameState: object expected");
            message.saveGameState = $root.SaveGameState.fromObject(object.saveGameState);
        }
        if (object.assignDamageResponse != null) {
            if (typeof object.assignDamageResponse !== "object")
                throw TypeError(".ClientGameMessage.assignDamageResponse: object expected");
            message.assignDamageResponse = $root.AssignDamageResponse.fromObject(object.assignDamageResponse);
        }
        if (object.selectKnowledgeResponse != null) {
            if (typeof object.selectKnowledgeResponse !== "object")
                throw TypeError(".ClientGameMessage.selectKnowledgeResponse: object expected");
            message.selectKnowledgeResponse = $root.SelectKnowledgeResponse.fromObject(object.selectKnowledgeResponse);
        }
        if (object.pickCardResponse != null) {
            if (typeof object.pickCardResponse !== "object")
                throw TypeError(".ClientGameMessage.pickCardResponse: object expected");
            message.pickCardResponse = $root.PickCardResponse.fromObject(object.pickCardResponse);
        }
        return message;
    };

    /**
     * Creates a plain object from a ClientGameMessage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof ClientGameMessage
     * @static
     * @param {ClientGameMessage} message ClientGameMessage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    ClientGameMessage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (message.playCard != null && message.hasOwnProperty("playCard")) {
            object.playCard = $root.PlayCard.toObject(message.playCard, options);
            if (options.oneofs)
                object.payload = "playCard";
        }
        if (message.activateCard != null && message.hasOwnProperty("activateCard")) {
            object.activateCard = $root.ActivateCard.toObject(message.activateCard, options);
            if (options.oneofs)
                object.payload = "activateCard";
        }
        if (message.studyCard != null && message.hasOwnProperty("studyCard")) {
            object.studyCard = $root.StudyCard.toObject(message.studyCard, options);
            if (options.oneofs)
                object.payload = "studyCard";
        }
        if (message.pass != null && message.hasOwnProperty("pass")) {
            object.pass = $root.Pass.toObject(message.pass, options);
            if (options.oneofs)
                object.payload = "pass";
        }
        if (message.redraw != null && message.hasOwnProperty("redraw")) {
            object.redraw = $root.Redraw.toObject(message.redraw, options);
            if (options.oneofs)
                object.payload = "redraw";
        }
        if (message.keep != null && message.hasOwnProperty("keep")) {
            object.keep = $root.Keep.toObject(message.keep, options);
            if (options.oneofs)
                object.payload = "keep";
        }
        if (message.selectFromResponse != null && message.hasOwnProperty("selectFromResponse")) {
            object.selectFromResponse = $root.SelectFromResponse.toObject(message.selectFromResponse, options);
            if (options.oneofs)
                object.payload = "selectFromResponse";
        }
        if (message.concede != null && message.hasOwnProperty("concede")) {
            object.concede = $root.Concede.toObject(message.concede, options);
            if (options.oneofs)
                object.payload = "concede";
        }
        if (message.orderCardsResponse != null && message.hasOwnProperty("orderCardsResponse")) {
            object.orderCardsResponse = $root.OrderCardsResponse.toObject(message.orderCardsResponse, options);
            if (options.oneofs)
                object.payload = "orderCardsResponse";
        }
        if (message.selectXValueResponse != null && message.hasOwnProperty("selectXValueResponse")) {
            object.selectXValueResponse = $root.SelectXValueResponse.toObject(message.selectXValueResponse, options);
            if (options.oneofs)
                object.payload = "selectXValueResponse";
        }
        if (message.selectAttackersResponse != null && message.hasOwnProperty("selectAttackersResponse")) {
            object.selectAttackersResponse = $root.SelectAttackersResponse.toObject(message.selectAttackersResponse, options);
            if (options.oneofs)
                object.payload = "selectAttackersResponse";
        }
        if (message.selectBlockersResponse != null && message.hasOwnProperty("selectBlockersResponse")) {
            object.selectBlockersResponse = $root.SelectBlockersResponse.toObject(message.selectBlockersResponse, options);
            if (options.oneofs)
                object.payload = "selectBlockersResponse";
        }
        if (message.saveGameState != null && message.hasOwnProperty("saveGameState")) {
            object.saveGameState = $root.SaveGameState.toObject(message.saveGameState, options);
            if (options.oneofs)
                object.payload = "saveGameState";
        }
        if (message.assignDamageResponse != null && message.hasOwnProperty("assignDamageResponse")) {
            object.assignDamageResponse = $root.AssignDamageResponse.toObject(message.assignDamageResponse, options);
            if (options.oneofs)
                object.payload = "assignDamageResponse";
        }
        if (message.selectKnowledgeResponse != null && message.hasOwnProperty("selectKnowledgeResponse")) {
            object.selectKnowledgeResponse = $root.SelectKnowledgeResponse.toObject(message.selectKnowledgeResponse, options);
            if (options.oneofs)
                object.payload = "selectKnowledgeResponse";
        }
        if (message.pickCardResponse != null && message.hasOwnProperty("pickCardResponse")) {
            object.pickCardResponse = $root.PickCardResponse.toObject(message.pickCardResponse, options);
            if (options.oneofs)
                object.payload = "pickCardResponse";
        }
        return object;
    };

    /**
     * Converts this ClientGameMessage to JSON.
     * @function toJSON
     * @memberof ClientGameMessage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    ClientGameMessage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return ClientGameMessage;
})();

/**
 * Custom type messages. Should not be sent directly.
 * @exports Phase
 * @enum {number}
 * @property {number} NOPHASE=0 NOPHASE value
 * @property {number} REDRAW=1 REDRAW value
 * @property {number} BEGIN=2 BEGIN value
 * @property {number} MAIN_BEFORE=3 MAIN_BEFORE value
 * @property {number} ATTACK=4 ATTACK value
 * @property {number} BLOCK=5 BLOCK value
 * @property {number} MAIN_AFTER=6 MAIN_AFTER value
 * @property {number} END=7 END value
 */
$root.Phase = (function() {
    var valuesById = {}, values = Object.create(valuesById);
    values[valuesById[0] = "NOPHASE"] = 0;
    values[valuesById[1] = "REDRAW"] = 1;
    values[valuesById[2] = "BEGIN"] = 2;
    values[valuesById[3] = "MAIN_BEFORE"] = 3;
    values[valuesById[4] = "ATTACK"] = 4;
    values[valuesById[5] = "BLOCK"] = 5;
    values[valuesById[6] = "MAIN_AFTER"] = 6;
    values[valuesById[7] = "END"] = 7;
    return values;
})();

/**
 * SelectFromType enum.
 * @exports SelectFromType
 * @enum {number}
 * @property {number} NOTYPE=0 NOTYPE value
 * @property {number} LIST=1 LIST value
 * @property {number} HAND=3 HAND value
 * @property {number} PLAY=4 PLAY value
 * @property {number} DISCARD_PILE=5 DISCARD_PILE value
 * @property {number} VOID=6 VOID value
 * @property {number} STACK=7 STACK value
 */
$root.SelectFromType = (function() {
    var valuesById = {}, values = Object.create(valuesById);
    values[valuesById[0] = "NOTYPE"] = 0;
    values[valuesById[1] = "LIST"] = 1;
    values[valuesById[3] = "HAND"] = 3;
    values[valuesById[4] = "PLAY"] = 4;
    values[valuesById[5] = "DISCARD_PILE"] = 5;
    values[valuesById[6] = "VOID"] = 6;
    values[valuesById[7] = "STACK"] = 7;
    return values;
})();

/**
 * GameType enum.
 * @exports GameType
 * @enum {number}
 * @property {number} NOGAME=0 NOGAME value
 * @property {number} BO1_CONSTRUCTED=1 BO1_CONSTRUCTED value
 * @property {number} P2_DRAFT=2 P2_DRAFT value
 * @property {number} P2_DRAFT_GAME=3 P2_DRAFT_GAME value
 */
$root.GameType = (function() {
    var valuesById = {}, values = Object.create(valuesById);
    values[valuesById[0] = "NOGAME"] = 0;
    values[valuesById[1] = "BO1_CONSTRUCTED"] = 1;
    values[valuesById[2] = "P2_DRAFT"] = 2;
    values[valuesById[3] = "P2_DRAFT_GAME"] = 3;
    return values;
})();

$root.Table = (function() {

    /**
     * Properties of a Table.
     * @exports ITable
     * @interface ITable
     * @property {string|null} [id] Table id
     * @property {string|null} [creator] Table creator
     * @property {string|null} [opponent] Table opponent
     */

    /**
     * Constructs a new Table.
     * @exports Table
     * @classdesc Represents a Table.
     * @implements ITable
     * @constructor
     * @param {ITable=} [properties] Properties to set
     */
    function Table(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Table id.
     * @member {string} id
     * @memberof Table
     * @instance
     */
    Table.prototype.id = "";

    /**
     * Table creator.
     * @member {string} creator
     * @memberof Table
     * @instance
     */
    Table.prototype.creator = "";

    /**
     * Table opponent.
     * @member {string} opponent
     * @memberof Table
     * @instance
     */
    Table.prototype.opponent = "";

    /**
     * Creates a new Table instance using the specified properties.
     * @function create
     * @memberof Table
     * @static
     * @param {ITable=} [properties] Properties to set
     * @returns {Table} Table instance
     */
    Table.create = function create(properties) {
        return new Table(properties);
    };

    /**
     * Encodes the specified Table message. Does not implicitly {@link Table.verify|verify} messages.
     * @function encode
     * @memberof Table
     * @static
     * @param {ITable} message Table message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Table.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.creator != null && Object.hasOwnProperty.call(message, "creator"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.creator);
        if (message.opponent != null && Object.hasOwnProperty.call(message, "opponent"))
            writer.uint32(/* id 3, wireType 2 =*/26).string(message.opponent);
        return writer;
    };

    /**
     * Encodes the specified Table message, length delimited. Does not implicitly {@link Table.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Table
     * @static
     * @param {ITable} message Table message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Table.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Table message from the specified reader or buffer.
     * @function decode
     * @memberof Table
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Table} Table
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Table.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Table();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.id = reader.string();
                break;
            case 2:
                message.creator = reader.string();
                break;
            case 3:
                message.opponent = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Table message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Table
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Table} Table
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Table.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Table message.
     * @function verify
     * @memberof Table
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Table.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.creator != null && message.hasOwnProperty("creator"))
            if (!$util.isString(message.creator))
                return "creator: string expected";
        if (message.opponent != null && message.hasOwnProperty("opponent"))
            if (!$util.isString(message.opponent))
                return "opponent: string expected";
        return null;
    };

    /**
     * Creates a Table message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Table
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Table} Table
     */
    Table.fromObject = function fromObject(object) {
        if (object instanceof $root.Table)
            return object;
        var message = new $root.Table();
        if (object.id != null)
            message.id = String(object.id);
        if (object.creator != null)
            message.creator = String(object.creator);
        if (object.opponent != null)
            message.opponent = String(object.opponent);
        return message;
    };

    /**
     * Creates a plain object from a Table message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Table
     * @static
     * @param {Table} message Table
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Table.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.id = "";
            object.creator = "";
            object.opponent = "";
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.creator != null && message.hasOwnProperty("creator"))
            object.creator = message.creator;
        if (message.opponent != null && message.hasOwnProperty("opponent"))
            object.opponent = message.opponent;
        return object;
    };

    /**
     * Converts this Table to JSON.
     * @function toJSON
     * @memberof Table
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Table.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Table;
})();

/**
 * Knowledge enum.
 * @exports Knowledge
 * @enum {number}
 * @property {number} NONE=0 NONE value
 * @property {number} PURPLE=1 PURPLE value
 * @property {number} GREEN=2 GREEN value
 * @property {number} RED=3 RED value
 * @property {number} BLUE=4 BLUE value
 * @property {number} YELLOW=5 YELLOW value
 */
$root.Knowledge = (function() {
    var valuesById = {}, values = Object.create(valuesById);
    values[valuesById[0] = "NONE"] = 0;
    values[valuesById[1] = "PURPLE"] = 1;
    values[valuesById[2] = "GREEN"] = 2;
    values[valuesById[3] = "RED"] = 3;
    values[valuesById[4] = "BLUE"] = 4;
    values[valuesById[5] = "YELLOW"] = 5;
    return values;
})();

$root.KnowledgeGroup = (function() {

    /**
     * Properties of a KnowledgeGroup.
     * @exports IKnowledgeGroup
     * @interface IKnowledgeGroup
     * @property {Knowledge|null} [knowledge] KnowledgeGroup knowledge
     * @property {number|null} [count] KnowledgeGroup count
     */

    /**
     * Constructs a new KnowledgeGroup.
     * @exports KnowledgeGroup
     * @classdesc Represents a KnowledgeGroup.
     * @implements IKnowledgeGroup
     * @constructor
     * @param {IKnowledgeGroup=} [properties] Properties to set
     */
    function KnowledgeGroup(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * KnowledgeGroup knowledge.
     * @member {Knowledge} knowledge
     * @memberof KnowledgeGroup
     * @instance
     */
    KnowledgeGroup.prototype.knowledge = 0;

    /**
     * KnowledgeGroup count.
     * @member {number} count
     * @memberof KnowledgeGroup
     * @instance
     */
    KnowledgeGroup.prototype.count = 0;

    /**
     * Creates a new KnowledgeGroup instance using the specified properties.
     * @function create
     * @memberof KnowledgeGroup
     * @static
     * @param {IKnowledgeGroup=} [properties] Properties to set
     * @returns {KnowledgeGroup} KnowledgeGroup instance
     */
    KnowledgeGroup.create = function create(properties) {
        return new KnowledgeGroup(properties);
    };

    /**
     * Encodes the specified KnowledgeGroup message. Does not implicitly {@link KnowledgeGroup.verify|verify} messages.
     * @function encode
     * @memberof KnowledgeGroup
     * @static
     * @param {IKnowledgeGroup} message KnowledgeGroup message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    KnowledgeGroup.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.knowledge != null && Object.hasOwnProperty.call(message, "knowledge"))
            writer.uint32(/* id 1, wireType 0 =*/8).int32(message.knowledge);
        if (message.count != null && Object.hasOwnProperty.call(message, "count"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.count);
        return writer;
    };

    /**
     * Encodes the specified KnowledgeGroup message, length delimited. Does not implicitly {@link KnowledgeGroup.verify|verify} messages.
     * @function encodeDelimited
     * @memberof KnowledgeGroup
     * @static
     * @param {IKnowledgeGroup} message KnowledgeGroup message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    KnowledgeGroup.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a KnowledgeGroup message from the specified reader or buffer.
     * @function decode
     * @memberof KnowledgeGroup
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {KnowledgeGroup} KnowledgeGroup
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    KnowledgeGroup.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.KnowledgeGroup();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.knowledge = reader.int32();
                break;
            case 2:
                message.count = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a KnowledgeGroup message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof KnowledgeGroup
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {KnowledgeGroup} KnowledgeGroup
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    KnowledgeGroup.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a KnowledgeGroup message.
     * @function verify
     * @memberof KnowledgeGroup
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    KnowledgeGroup.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.knowledge != null && message.hasOwnProperty("knowledge"))
            switch (message.knowledge) {
            default:
                return "knowledge: enum value expected";
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            }
        if (message.count != null && message.hasOwnProperty("count"))
            if (!$util.isInteger(message.count))
                return "count: integer expected";
        return null;
    };

    /**
     * Creates a KnowledgeGroup message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof KnowledgeGroup
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {KnowledgeGroup} KnowledgeGroup
     */
    KnowledgeGroup.fromObject = function fromObject(object) {
        if (object instanceof $root.KnowledgeGroup)
            return object;
        var message = new $root.KnowledgeGroup();
        switch (object.knowledge) {
        case "NONE":
        case 0:
            message.knowledge = 0;
            break;
        case "PURPLE":
        case 1:
            message.knowledge = 1;
            break;
        case "GREEN":
        case 2:
            message.knowledge = 2;
            break;
        case "RED":
        case 3:
            message.knowledge = 3;
            break;
        case "BLUE":
        case 4:
            message.knowledge = 4;
            break;
        case "YELLOW":
        case 5:
            message.knowledge = 5;
            break;
        }
        if (object.count != null)
            message.count = object.count | 0;
        return message;
    };

    /**
     * Creates a plain object from a KnowledgeGroup message. Also converts values to other types if specified.
     * @function toObject
     * @memberof KnowledgeGroup
     * @static
     * @param {KnowledgeGroup} message KnowledgeGroup
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    KnowledgeGroup.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.knowledge = options.enums === String ? "NONE" : 0;
            object.count = 0;
        }
        if (message.knowledge != null && message.hasOwnProperty("knowledge"))
            object.knowledge = options.enums === String ? $root.Knowledge[message.knowledge] : message.knowledge;
        if (message.count != null && message.hasOwnProperty("count"))
            object.count = message.count;
        return object;
    };

    /**
     * Converts this KnowledgeGroup to JSON.
     * @function toJSON
     * @memberof KnowledgeGroup
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    KnowledgeGroup.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return KnowledgeGroup;
})();

/**
 * Counter enum.
 * @exports Counter
 * @enum {number}
 * @property {number} NOCOUNTER=0 NOCOUNTER value
 * @property {number} CHARGE=1 CHARGE value
 */
$root.Counter = (function() {
    var valuesById = {}, values = Object.create(valuesById);
    values[valuesById[0] = "NOCOUNTER"] = 0;
    values[valuesById[1] = "CHARGE"] = 1;
    return values;
})();

$root.CounterGroup = (function() {

    /**
     * Properties of a CounterGroup.
     * @exports ICounterGroup
     * @interface ICounterGroup
     * @property {Counter|null} [counter] CounterGroup counter
     * @property {number|null} [count] CounterGroup count
     */

    /**
     * Constructs a new CounterGroup.
     * @exports CounterGroup
     * @classdesc Represents a CounterGroup.
     * @implements ICounterGroup
     * @constructor
     * @param {ICounterGroup=} [properties] Properties to set
     */
    function CounterGroup(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * CounterGroup counter.
     * @member {Counter} counter
     * @memberof CounterGroup
     * @instance
     */
    CounterGroup.prototype.counter = 0;

    /**
     * CounterGroup count.
     * @member {number} count
     * @memberof CounterGroup
     * @instance
     */
    CounterGroup.prototype.count = 0;

    /**
     * Creates a new CounterGroup instance using the specified properties.
     * @function create
     * @memberof CounterGroup
     * @static
     * @param {ICounterGroup=} [properties] Properties to set
     * @returns {CounterGroup} CounterGroup instance
     */
    CounterGroup.create = function create(properties) {
        return new CounterGroup(properties);
    };

    /**
     * Encodes the specified CounterGroup message. Does not implicitly {@link CounterGroup.verify|verify} messages.
     * @function encode
     * @memberof CounterGroup
     * @static
     * @param {ICounterGroup} message CounterGroup message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    CounterGroup.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.counter != null && Object.hasOwnProperty.call(message, "counter"))
            writer.uint32(/* id 1, wireType 0 =*/8).int32(message.counter);
        if (message.count != null && Object.hasOwnProperty.call(message, "count"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.count);
        return writer;
    };

    /**
     * Encodes the specified CounterGroup message, length delimited. Does not implicitly {@link CounterGroup.verify|verify} messages.
     * @function encodeDelimited
     * @memberof CounterGroup
     * @static
     * @param {ICounterGroup} message CounterGroup message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    CounterGroup.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a CounterGroup message from the specified reader or buffer.
     * @function decode
     * @memberof CounterGroup
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {CounterGroup} CounterGroup
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    CounterGroup.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.CounterGroup();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.counter = reader.int32();
                break;
            case 2:
                message.count = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a CounterGroup message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof CounterGroup
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {CounterGroup} CounterGroup
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    CounterGroup.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a CounterGroup message.
     * @function verify
     * @memberof CounterGroup
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    CounterGroup.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.counter != null && message.hasOwnProperty("counter"))
            switch (message.counter) {
            default:
                return "counter: enum value expected";
            case 0:
            case 1:
                break;
            }
        if (message.count != null && message.hasOwnProperty("count"))
            if (!$util.isInteger(message.count))
                return "count: integer expected";
        return null;
    };

    /**
     * Creates a CounterGroup message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof CounterGroup
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {CounterGroup} CounterGroup
     */
    CounterGroup.fromObject = function fromObject(object) {
        if (object instanceof $root.CounterGroup)
            return object;
        var message = new $root.CounterGroup();
        switch (object.counter) {
        case "NOCOUNTER":
        case 0:
            message.counter = 0;
            break;
        case "CHARGE":
        case 1:
            message.counter = 1;
            break;
        }
        if (object.count != null)
            message.count = object.count | 0;
        return message;
    };

    /**
     * Creates a plain object from a CounterGroup message. Also converts values to other types if specified.
     * @function toObject
     * @memberof CounterGroup
     * @static
     * @param {CounterGroup} message CounterGroup
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    CounterGroup.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.counter = options.enums === String ? "NOCOUNTER" : 0;
            object.count = 0;
        }
        if (message.counter != null && message.hasOwnProperty("counter"))
            object.counter = options.enums === String ? $root.Counter[message.counter] : message.counter;
        if (message.count != null && message.hasOwnProperty("count"))
            object.count = message.count;
        return object;
    };

    /**
     * Converts this CounterGroup to JSON.
     * @function toJSON
     * @memberof CounterGroup
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    CounterGroup.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return CounterGroup;
})();

$root.Blocker = (function() {

    /**
     * Properties of a Blocker.
     * @exports IBlocker
     * @interface IBlocker
     * @property {string|null} [blockerId] Blocker blockerId
     * @property {Array.<string>|null} [possibleBlockTargets] Blocker possibleBlockTargets
     */

    /**
     * Constructs a new Blocker.
     * @exports Blocker
     * @classdesc Represents a Blocker.
     * @implements IBlocker
     * @constructor
     * @param {IBlocker=} [properties] Properties to set
     */
    function Blocker(properties) {
        this.possibleBlockTargets = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Blocker blockerId.
     * @member {string} blockerId
     * @memberof Blocker
     * @instance
     */
    Blocker.prototype.blockerId = "";

    /**
     * Blocker possibleBlockTargets.
     * @member {Array.<string>} possibleBlockTargets
     * @memberof Blocker
     * @instance
     */
    Blocker.prototype.possibleBlockTargets = $util.emptyArray;

    /**
     * Creates a new Blocker instance using the specified properties.
     * @function create
     * @memberof Blocker
     * @static
     * @param {IBlocker=} [properties] Properties to set
     * @returns {Blocker} Blocker instance
     */
    Blocker.create = function create(properties) {
        return new Blocker(properties);
    };

    /**
     * Encodes the specified Blocker message. Does not implicitly {@link Blocker.verify|verify} messages.
     * @function encode
     * @memberof Blocker
     * @static
     * @param {IBlocker} message Blocker message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Blocker.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.blockerId != null && Object.hasOwnProperty.call(message, "blockerId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.blockerId);
        if (message.possibleBlockTargets != null && message.possibleBlockTargets.length)
            for (var i = 0; i < message.possibleBlockTargets.length; ++i)
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.possibleBlockTargets[i]);
        return writer;
    };

    /**
     * Encodes the specified Blocker message, length delimited. Does not implicitly {@link Blocker.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Blocker
     * @static
     * @param {IBlocker} message Blocker message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Blocker.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Blocker message from the specified reader or buffer.
     * @function decode
     * @memberof Blocker
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Blocker} Blocker
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Blocker.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Blocker();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.blockerId = reader.string();
                break;
            case 2:
                if (!(message.possibleBlockTargets && message.possibleBlockTargets.length))
                    message.possibleBlockTargets = [];
                message.possibleBlockTargets.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Blocker message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Blocker
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Blocker} Blocker
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Blocker.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Blocker message.
     * @function verify
     * @memberof Blocker
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Blocker.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.blockerId != null && message.hasOwnProperty("blockerId"))
            if (!$util.isString(message.blockerId))
                return "blockerId: string expected";
        if (message.possibleBlockTargets != null && message.hasOwnProperty("possibleBlockTargets")) {
            if (!Array.isArray(message.possibleBlockTargets))
                return "possibleBlockTargets: array expected";
            for (var i = 0; i < message.possibleBlockTargets.length; ++i)
                if (!$util.isString(message.possibleBlockTargets[i]))
                    return "possibleBlockTargets: string[] expected";
        }
        return null;
    };

    /**
     * Creates a Blocker message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Blocker
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Blocker} Blocker
     */
    Blocker.fromObject = function fromObject(object) {
        if (object instanceof $root.Blocker)
            return object;
        var message = new $root.Blocker();
        if (object.blockerId != null)
            message.blockerId = String(object.blockerId);
        if (object.possibleBlockTargets) {
            if (!Array.isArray(object.possibleBlockTargets))
                throw TypeError(".Blocker.possibleBlockTargets: array expected");
            message.possibleBlockTargets = [];
            for (var i = 0; i < object.possibleBlockTargets.length; ++i)
                message.possibleBlockTargets[i] = String(object.possibleBlockTargets[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from a Blocker message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Blocker
     * @static
     * @param {Blocker} message Blocker
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Blocker.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.possibleBlockTargets = [];
        if (options.defaults)
            object.blockerId = "";
        if (message.blockerId != null && message.hasOwnProperty("blockerId"))
            object.blockerId = message.blockerId;
        if (message.possibleBlockTargets && message.possibleBlockTargets.length) {
            object.possibleBlockTargets = [];
            for (var j = 0; j < message.possibleBlockTargets.length; ++j)
                object.possibleBlockTargets[j] = message.possibleBlockTargets[j];
        }
        return object;
    };

    /**
     * Converts this Blocker to JSON.
     * @function toJSON
     * @memberof Blocker
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Blocker.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Blocker;
})();

$root.Attacker = (function() {

    /**
     * Properties of an Attacker.
     * @exports IAttacker
     * @interface IAttacker
     * @property {string|null} [attackerId] Attacker attackerId
     * @property {Array.<string>|null} [possibleAttackTargets] Attacker possibleAttackTargets
     */

    /**
     * Constructs a new Attacker.
     * @exports Attacker
     * @classdesc Represents an Attacker.
     * @implements IAttacker
     * @constructor
     * @param {IAttacker=} [properties] Properties to set
     */
    function Attacker(properties) {
        this.possibleAttackTargets = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Attacker attackerId.
     * @member {string} attackerId
     * @memberof Attacker
     * @instance
     */
    Attacker.prototype.attackerId = "";

    /**
     * Attacker possibleAttackTargets.
     * @member {Array.<string>} possibleAttackTargets
     * @memberof Attacker
     * @instance
     */
    Attacker.prototype.possibleAttackTargets = $util.emptyArray;

    /**
     * Creates a new Attacker instance using the specified properties.
     * @function create
     * @memberof Attacker
     * @static
     * @param {IAttacker=} [properties] Properties to set
     * @returns {Attacker} Attacker instance
     */
    Attacker.create = function create(properties) {
        return new Attacker(properties);
    };

    /**
     * Encodes the specified Attacker message. Does not implicitly {@link Attacker.verify|verify} messages.
     * @function encode
     * @memberof Attacker
     * @static
     * @param {IAttacker} message Attacker message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Attacker.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.attackerId != null && Object.hasOwnProperty.call(message, "attackerId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.attackerId);
        if (message.possibleAttackTargets != null && message.possibleAttackTargets.length)
            for (var i = 0; i < message.possibleAttackTargets.length; ++i)
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.possibleAttackTargets[i]);
        return writer;
    };

    /**
     * Encodes the specified Attacker message, length delimited. Does not implicitly {@link Attacker.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Attacker
     * @static
     * @param {IAttacker} message Attacker message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Attacker.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an Attacker message from the specified reader or buffer.
     * @function decode
     * @memberof Attacker
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Attacker} Attacker
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Attacker.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Attacker();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.attackerId = reader.string();
                break;
            case 2:
                if (!(message.possibleAttackTargets && message.possibleAttackTargets.length))
                    message.possibleAttackTargets = [];
                message.possibleAttackTargets.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an Attacker message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Attacker
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Attacker} Attacker
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Attacker.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an Attacker message.
     * @function verify
     * @memberof Attacker
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Attacker.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.attackerId != null && message.hasOwnProperty("attackerId"))
            if (!$util.isString(message.attackerId))
                return "attackerId: string expected";
        if (message.possibleAttackTargets != null && message.hasOwnProperty("possibleAttackTargets")) {
            if (!Array.isArray(message.possibleAttackTargets))
                return "possibleAttackTargets: array expected";
            for (var i = 0; i < message.possibleAttackTargets.length; ++i)
                if (!$util.isString(message.possibleAttackTargets[i]))
                    return "possibleAttackTargets: string[] expected";
        }
        return null;
    };

    /**
     * Creates an Attacker message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Attacker
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Attacker} Attacker
     */
    Attacker.fromObject = function fromObject(object) {
        if (object instanceof $root.Attacker)
            return object;
        var message = new $root.Attacker();
        if (object.attackerId != null)
            message.attackerId = String(object.attackerId);
        if (object.possibleAttackTargets) {
            if (!Array.isArray(object.possibleAttackTargets))
                throw TypeError(".Attacker.possibleAttackTargets: array expected");
            message.possibleAttackTargets = [];
            for (var i = 0; i < object.possibleAttackTargets.length; ++i)
                message.possibleAttackTargets[i] = String(object.possibleAttackTargets[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from an Attacker message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Attacker
     * @static
     * @param {Attacker} message Attacker
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Attacker.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.possibleAttackTargets = [];
        if (options.defaults)
            object.attackerId = "";
        if (message.attackerId != null && message.hasOwnProperty("attackerId"))
            object.attackerId = message.attackerId;
        if (message.possibleAttackTargets && message.possibleAttackTargets.length) {
            object.possibleAttackTargets = [];
            for (var j = 0; j < message.possibleAttackTargets.length; ++j)
                object.possibleAttackTargets[j] = message.possibleAttackTargets[j];
        }
        return object;
    };

    /**
     * Converts this Attacker to JSON.
     * @function toJSON
     * @memberof Attacker
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Attacker.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Attacker;
})();

$root.AttackerAssignment = (function() {

    /**
     * Properties of an AttackerAssignment.
     * @exports IAttackerAssignment
     * @interface IAttackerAssignment
     * @property {string|null} [attackerId] AttackerAssignment attackerId
     * @property {string|null} [attacksTo] AttackerAssignment attacksTo
     */

    /**
     * Constructs a new AttackerAssignment.
     * @exports AttackerAssignment
     * @classdesc Represents an AttackerAssignment.
     * @implements IAttackerAssignment
     * @constructor
     * @param {IAttackerAssignment=} [properties] Properties to set
     */
    function AttackerAssignment(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * AttackerAssignment attackerId.
     * @member {string} attackerId
     * @memberof AttackerAssignment
     * @instance
     */
    AttackerAssignment.prototype.attackerId = "";

    /**
     * AttackerAssignment attacksTo.
     * @member {string} attacksTo
     * @memberof AttackerAssignment
     * @instance
     */
    AttackerAssignment.prototype.attacksTo = "";

    /**
     * Creates a new AttackerAssignment instance using the specified properties.
     * @function create
     * @memberof AttackerAssignment
     * @static
     * @param {IAttackerAssignment=} [properties] Properties to set
     * @returns {AttackerAssignment} AttackerAssignment instance
     */
    AttackerAssignment.create = function create(properties) {
        return new AttackerAssignment(properties);
    };

    /**
     * Encodes the specified AttackerAssignment message. Does not implicitly {@link AttackerAssignment.verify|verify} messages.
     * @function encode
     * @memberof AttackerAssignment
     * @static
     * @param {IAttackerAssignment} message AttackerAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AttackerAssignment.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.attackerId != null && Object.hasOwnProperty.call(message, "attackerId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.attackerId);
        if (message.attacksTo != null && Object.hasOwnProperty.call(message, "attacksTo"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.attacksTo);
        return writer;
    };

    /**
     * Encodes the specified AttackerAssignment message, length delimited. Does not implicitly {@link AttackerAssignment.verify|verify} messages.
     * @function encodeDelimited
     * @memberof AttackerAssignment
     * @static
     * @param {IAttackerAssignment} message AttackerAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AttackerAssignment.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an AttackerAssignment message from the specified reader or buffer.
     * @function decode
     * @memberof AttackerAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {AttackerAssignment} AttackerAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AttackerAssignment.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.AttackerAssignment();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.attackerId = reader.string();
                break;
            case 2:
                message.attacksTo = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an AttackerAssignment message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof AttackerAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {AttackerAssignment} AttackerAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AttackerAssignment.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an AttackerAssignment message.
     * @function verify
     * @memberof AttackerAssignment
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    AttackerAssignment.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.attackerId != null && message.hasOwnProperty("attackerId"))
            if (!$util.isString(message.attackerId))
                return "attackerId: string expected";
        if (message.attacksTo != null && message.hasOwnProperty("attacksTo"))
            if (!$util.isString(message.attacksTo))
                return "attacksTo: string expected";
        return null;
    };

    /**
     * Creates an AttackerAssignment message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof AttackerAssignment
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {AttackerAssignment} AttackerAssignment
     */
    AttackerAssignment.fromObject = function fromObject(object) {
        if (object instanceof $root.AttackerAssignment)
            return object;
        var message = new $root.AttackerAssignment();
        if (object.attackerId != null)
            message.attackerId = String(object.attackerId);
        if (object.attacksTo != null)
            message.attacksTo = String(object.attacksTo);
        return message;
    };

    /**
     * Creates a plain object from an AttackerAssignment message. Also converts values to other types if specified.
     * @function toObject
     * @memberof AttackerAssignment
     * @static
     * @param {AttackerAssignment} message AttackerAssignment
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    AttackerAssignment.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.attackerId = "";
            object.attacksTo = "";
        }
        if (message.attackerId != null && message.hasOwnProperty("attackerId"))
            object.attackerId = message.attackerId;
        if (message.attacksTo != null && message.hasOwnProperty("attacksTo"))
            object.attacksTo = message.attacksTo;
        return object;
    };

    /**
     * Converts this AttackerAssignment to JSON.
     * @function toJSON
     * @memberof AttackerAssignment
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    AttackerAssignment.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return AttackerAssignment;
})();

$root.BlockerAssignment = (function() {

    /**
     * Properties of a BlockerAssignment.
     * @exports IBlockerAssignment
     * @interface IBlockerAssignment
     * @property {string|null} [blockerId] BlockerAssignment blockerId
     * @property {string|null} [blockedBy] BlockerAssignment blockedBy
     */

    /**
     * Constructs a new BlockerAssignment.
     * @exports BlockerAssignment
     * @classdesc Represents a BlockerAssignment.
     * @implements IBlockerAssignment
     * @constructor
     * @param {IBlockerAssignment=} [properties] Properties to set
     */
    function BlockerAssignment(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * BlockerAssignment blockerId.
     * @member {string} blockerId
     * @memberof BlockerAssignment
     * @instance
     */
    BlockerAssignment.prototype.blockerId = "";

    /**
     * BlockerAssignment blockedBy.
     * @member {string} blockedBy
     * @memberof BlockerAssignment
     * @instance
     */
    BlockerAssignment.prototype.blockedBy = "";

    /**
     * Creates a new BlockerAssignment instance using the specified properties.
     * @function create
     * @memberof BlockerAssignment
     * @static
     * @param {IBlockerAssignment=} [properties] Properties to set
     * @returns {BlockerAssignment} BlockerAssignment instance
     */
    BlockerAssignment.create = function create(properties) {
        return new BlockerAssignment(properties);
    };

    /**
     * Encodes the specified BlockerAssignment message. Does not implicitly {@link BlockerAssignment.verify|verify} messages.
     * @function encode
     * @memberof BlockerAssignment
     * @static
     * @param {IBlockerAssignment} message BlockerAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    BlockerAssignment.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.blockerId != null && Object.hasOwnProperty.call(message, "blockerId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.blockerId);
        if (message.blockedBy != null && Object.hasOwnProperty.call(message, "blockedBy"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.blockedBy);
        return writer;
    };

    /**
     * Encodes the specified BlockerAssignment message, length delimited. Does not implicitly {@link BlockerAssignment.verify|verify} messages.
     * @function encodeDelimited
     * @memberof BlockerAssignment
     * @static
     * @param {IBlockerAssignment} message BlockerAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    BlockerAssignment.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a BlockerAssignment message from the specified reader or buffer.
     * @function decode
     * @memberof BlockerAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {BlockerAssignment} BlockerAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    BlockerAssignment.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.BlockerAssignment();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.blockerId = reader.string();
                break;
            case 2:
                message.blockedBy = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a BlockerAssignment message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof BlockerAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {BlockerAssignment} BlockerAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    BlockerAssignment.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a BlockerAssignment message.
     * @function verify
     * @memberof BlockerAssignment
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    BlockerAssignment.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.blockerId != null && message.hasOwnProperty("blockerId"))
            if (!$util.isString(message.blockerId))
                return "blockerId: string expected";
        if (message.blockedBy != null && message.hasOwnProperty("blockedBy"))
            if (!$util.isString(message.blockedBy))
                return "blockedBy: string expected";
        return null;
    };

    /**
     * Creates a BlockerAssignment message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof BlockerAssignment
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {BlockerAssignment} BlockerAssignment
     */
    BlockerAssignment.fromObject = function fromObject(object) {
        if (object instanceof $root.BlockerAssignment)
            return object;
        var message = new $root.BlockerAssignment();
        if (object.blockerId != null)
            message.blockerId = String(object.blockerId);
        if (object.blockedBy != null)
            message.blockedBy = String(object.blockedBy);
        return message;
    };

    /**
     * Creates a plain object from a BlockerAssignment message. Also converts values to other types if specified.
     * @function toObject
     * @memberof BlockerAssignment
     * @static
     * @param {BlockerAssignment} message BlockerAssignment
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    BlockerAssignment.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.blockerId = "";
            object.blockedBy = "";
        }
        if (message.blockerId != null && message.hasOwnProperty("blockerId"))
            object.blockerId = message.blockerId;
        if (message.blockedBy != null && message.hasOwnProperty("blockedBy"))
            object.blockedBy = message.blockedBy;
        return object;
    };

    /**
     * Converts this BlockerAssignment to JSON.
     * @function toJSON
     * @memberof BlockerAssignment
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    BlockerAssignment.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return BlockerAssignment;
})();

$root.DamageAssignment = (function() {

    /**
     * Properties of a DamageAssignment.
     * @exports IDamageAssignment
     * @interface IDamageAssignment
     * @property {string|null} [targetId] DamageAssignment targetId
     * @property {number|null} [damage] DamageAssignment damage
     */

    /**
     * Constructs a new DamageAssignment.
     * @exports DamageAssignment
     * @classdesc Represents a DamageAssignment.
     * @implements IDamageAssignment
     * @constructor
     * @param {IDamageAssignment=} [properties] Properties to set
     */
    function DamageAssignment(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * DamageAssignment targetId.
     * @member {string} targetId
     * @memberof DamageAssignment
     * @instance
     */
    DamageAssignment.prototype.targetId = "";

    /**
     * DamageAssignment damage.
     * @member {number} damage
     * @memberof DamageAssignment
     * @instance
     */
    DamageAssignment.prototype.damage = 0;

    /**
     * Creates a new DamageAssignment instance using the specified properties.
     * @function create
     * @memberof DamageAssignment
     * @static
     * @param {IDamageAssignment=} [properties] Properties to set
     * @returns {DamageAssignment} DamageAssignment instance
     */
    DamageAssignment.create = function create(properties) {
        return new DamageAssignment(properties);
    };

    /**
     * Encodes the specified DamageAssignment message. Does not implicitly {@link DamageAssignment.verify|verify} messages.
     * @function encode
     * @memberof DamageAssignment
     * @static
     * @param {IDamageAssignment} message DamageAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    DamageAssignment.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.targetId != null && Object.hasOwnProperty.call(message, "targetId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.targetId);
        if (message.damage != null && Object.hasOwnProperty.call(message, "damage"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.damage);
        return writer;
    };

    /**
     * Encodes the specified DamageAssignment message, length delimited. Does not implicitly {@link DamageAssignment.verify|verify} messages.
     * @function encodeDelimited
     * @memberof DamageAssignment
     * @static
     * @param {IDamageAssignment} message DamageAssignment message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    DamageAssignment.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a DamageAssignment message from the specified reader or buffer.
     * @function decode
     * @memberof DamageAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {DamageAssignment} DamageAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    DamageAssignment.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.DamageAssignment();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.targetId = reader.string();
                break;
            case 2:
                message.damage = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a DamageAssignment message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof DamageAssignment
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {DamageAssignment} DamageAssignment
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    DamageAssignment.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a DamageAssignment message.
     * @function verify
     * @memberof DamageAssignment
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    DamageAssignment.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.targetId != null && message.hasOwnProperty("targetId"))
            if (!$util.isString(message.targetId))
                return "targetId: string expected";
        if (message.damage != null && message.hasOwnProperty("damage"))
            if (!$util.isInteger(message.damage))
                return "damage: integer expected";
        return null;
    };

    /**
     * Creates a DamageAssignment message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof DamageAssignment
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {DamageAssignment} DamageAssignment
     */
    DamageAssignment.fromObject = function fromObject(object) {
        if (object instanceof $root.DamageAssignment)
            return object;
        var message = new $root.DamageAssignment();
        if (object.targetId != null)
            message.targetId = String(object.targetId);
        if (object.damage != null)
            message.damage = object.damage | 0;
        return message;
    };

    /**
     * Creates a plain object from a DamageAssignment message. Also converts values to other types if specified.
     * @function toObject
     * @memberof DamageAssignment
     * @static
     * @param {DamageAssignment} message DamageAssignment
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    DamageAssignment.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.targetId = "";
            object.damage = 0;
        }
        if (message.targetId != null && message.hasOwnProperty("targetId"))
            object.targetId = message.targetId;
        if (message.damage != null && message.hasOwnProperty("damage"))
            object.damage = message.damage;
        return object;
    };

    /**
     * Converts this DamageAssignment to JSON.
     * @function toJSON
     * @memberof DamageAssignment
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    DamageAssignment.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return DamageAssignment;
})();

$root.Combat = (function() {

    /**
     * Properties of a Combat.
     * @exports ICombat
     * @interface ICombat
     * @property {number|null} [health] Combat health
     * @property {number|null} [shield] Combat shield
     * @property {number|null} [attack] Combat attack
     * @property {boolean|null} [deploying] Combat deploying
     * @property {string|null} [blockedAttacker] Combat blockedAttacker
     * @property {string|null} [attackTarget] Combat attackTarget
     * @property {Array.<string>|null} [combatAbilities] Combat combatAbilities
     */

    /**
     * Constructs a new Combat.
     * @exports Combat
     * @classdesc Represents a Combat.
     * @implements ICombat
     * @constructor
     * @param {ICombat=} [properties] Properties to set
     */
    function Combat(properties) {
        this.combatAbilities = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Combat health.
     * @member {number} health
     * @memberof Combat
     * @instance
     */
    Combat.prototype.health = 0;

    /**
     * Combat shield.
     * @member {number} shield
     * @memberof Combat
     * @instance
     */
    Combat.prototype.shield = 0;

    /**
     * Combat attack.
     * @member {number} attack
     * @memberof Combat
     * @instance
     */
    Combat.prototype.attack = 0;

    /**
     * Combat deploying.
     * @member {boolean} deploying
     * @memberof Combat
     * @instance
     */
    Combat.prototype.deploying = false;

    /**
     * Combat blockedAttacker.
     * @member {string} blockedAttacker
     * @memberof Combat
     * @instance
     */
    Combat.prototype.blockedAttacker = "";

    /**
     * Combat attackTarget.
     * @member {string} attackTarget
     * @memberof Combat
     * @instance
     */
    Combat.prototype.attackTarget = "";

    /**
     * Combat combatAbilities.
     * @member {Array.<string>} combatAbilities
     * @memberof Combat
     * @instance
     */
    Combat.prototype.combatAbilities = $util.emptyArray;

    /**
     * Creates a new Combat instance using the specified properties.
     * @function create
     * @memberof Combat
     * @static
     * @param {ICombat=} [properties] Properties to set
     * @returns {Combat} Combat instance
     */
    Combat.create = function create(properties) {
        return new Combat(properties);
    };

    /**
     * Encodes the specified Combat message. Does not implicitly {@link Combat.verify|verify} messages.
     * @function encode
     * @memberof Combat
     * @static
     * @param {ICombat} message Combat message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Combat.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.health != null && Object.hasOwnProperty.call(message, "health"))
            writer.uint32(/* id 12, wireType 0 =*/96).int32(message.health);
        if (message.shield != null && Object.hasOwnProperty.call(message, "shield"))
            writer.uint32(/* id 15, wireType 0 =*/120).int32(message.shield);
        if (message.attack != null && Object.hasOwnProperty.call(message, "attack"))
            writer.uint32(/* id 17, wireType 0 =*/136).int32(message.attack);
        if (message.deploying != null && Object.hasOwnProperty.call(message, "deploying"))
            writer.uint32(/* id 18, wireType 0 =*/144).bool(message.deploying);
        if (message.blockedAttacker != null && Object.hasOwnProperty.call(message, "blockedAttacker"))
            writer.uint32(/* id 19, wireType 2 =*/154).string(message.blockedAttacker);
        if (message.attackTarget != null && Object.hasOwnProperty.call(message, "attackTarget"))
            writer.uint32(/* id 20, wireType 2 =*/162).string(message.attackTarget);
        if (message.combatAbilities != null && message.combatAbilities.length)
            for (var i = 0; i < message.combatAbilities.length; ++i)
                writer.uint32(/* id 21, wireType 2 =*/170).string(message.combatAbilities[i]);
        return writer;
    };

    /**
     * Encodes the specified Combat message, length delimited. Does not implicitly {@link Combat.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Combat
     * @static
     * @param {ICombat} message Combat message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Combat.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Combat message from the specified reader or buffer.
     * @function decode
     * @memberof Combat
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Combat} Combat
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Combat.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Combat();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 12:
                message.health = reader.int32();
                break;
            case 15:
                message.shield = reader.int32();
                break;
            case 17:
                message.attack = reader.int32();
                break;
            case 18:
                message.deploying = reader.bool();
                break;
            case 19:
                message.blockedAttacker = reader.string();
                break;
            case 20:
                message.attackTarget = reader.string();
                break;
            case 21:
                if (!(message.combatAbilities && message.combatAbilities.length))
                    message.combatAbilities = [];
                message.combatAbilities.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Combat message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Combat
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Combat} Combat
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Combat.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Combat message.
     * @function verify
     * @memberof Combat
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Combat.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.health != null && message.hasOwnProperty("health"))
            if (!$util.isInteger(message.health))
                return "health: integer expected";
        if (message.shield != null && message.hasOwnProperty("shield"))
            if (!$util.isInteger(message.shield))
                return "shield: integer expected";
        if (message.attack != null && message.hasOwnProperty("attack"))
            if (!$util.isInteger(message.attack))
                return "attack: integer expected";
        if (message.deploying != null && message.hasOwnProperty("deploying"))
            if (typeof message.deploying !== "boolean")
                return "deploying: boolean expected";
        if (message.blockedAttacker != null && message.hasOwnProperty("blockedAttacker"))
            if (!$util.isString(message.blockedAttacker))
                return "blockedAttacker: string expected";
        if (message.attackTarget != null && message.hasOwnProperty("attackTarget"))
            if (!$util.isString(message.attackTarget))
                return "attackTarget: string expected";
        if (message.combatAbilities != null && message.hasOwnProperty("combatAbilities")) {
            if (!Array.isArray(message.combatAbilities))
                return "combatAbilities: array expected";
            for (var i = 0; i < message.combatAbilities.length; ++i)
                if (!$util.isString(message.combatAbilities[i]))
                    return "combatAbilities: string[] expected";
        }
        return null;
    };

    /**
     * Creates a Combat message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Combat
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Combat} Combat
     */
    Combat.fromObject = function fromObject(object) {
        if (object instanceof $root.Combat)
            return object;
        var message = new $root.Combat();
        if (object.health != null)
            message.health = object.health | 0;
        if (object.shield != null)
            message.shield = object.shield | 0;
        if (object.attack != null)
            message.attack = object.attack | 0;
        if (object.deploying != null)
            message.deploying = Boolean(object.deploying);
        if (object.blockedAttacker != null)
            message.blockedAttacker = String(object.blockedAttacker);
        if (object.attackTarget != null)
            message.attackTarget = String(object.attackTarget);
        if (object.combatAbilities) {
            if (!Array.isArray(object.combatAbilities))
                throw TypeError(".Combat.combatAbilities: array expected");
            message.combatAbilities = [];
            for (var i = 0; i < object.combatAbilities.length; ++i)
                message.combatAbilities[i] = String(object.combatAbilities[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from a Combat message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Combat
     * @static
     * @param {Combat} message Combat
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Combat.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.combatAbilities = [];
        if (options.defaults) {
            object.health = 0;
            object.shield = 0;
            object.attack = 0;
            object.deploying = false;
            object.blockedAttacker = "";
            object.attackTarget = "";
        }
        if (message.health != null && message.hasOwnProperty("health"))
            object.health = message.health;
        if (message.shield != null && message.hasOwnProperty("shield"))
            object.shield = message.shield;
        if (message.attack != null && message.hasOwnProperty("attack"))
            object.attack = message.attack;
        if (message.deploying != null && message.hasOwnProperty("deploying"))
            object.deploying = message.deploying;
        if (message.blockedAttacker != null && message.hasOwnProperty("blockedAttacker"))
            object.blockedAttacker = message.blockedAttacker;
        if (message.attackTarget != null && message.hasOwnProperty("attackTarget"))
            object.attackTarget = message.attackTarget;
        if (message.combatAbilities && message.combatAbilities.length) {
            object.combatAbilities = [];
            for (var j = 0; j < message.combatAbilities.length; ++j)
                object.combatAbilities[j] = message.combatAbilities[j];
        }
        return object;
    };

    /**
     * Converts this Combat to JSON.
     * @function toJSON
     * @memberof Combat
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Combat.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Combat;
})();

$root.Card = (function() {

    /**
     * Properties of a Card.
     * @exports ICard
     * @interface ICard
     * @property {string|null} [id] Card id
     * @property {string|null} [name] Card name
     * @property {Array.<ICounterGroup>|null} [counters] Card counters
     * @property {boolean|null} [depleted] Card depleted
     * @property {boolean|null} [marked] Card marked
     * @property {Array.<string>|null} [targets] Card targets
     * @property {string|null} [description] Card description
     * @property {string|null} [cost] Card cost
     * @property {Array.<IKnowledgeGroup>|null} [knowledgeCost] Card knowledgeCost
     * @property {Array.<string>|null} [types] Card types
     * @property {Array.<string>|null} [subtypes] Card subtypes
     * @property {number|null} [delay] Card delay
     * @property {number|null} [loyalty] Card loyalty
     * @property {ICombat|null} [combat] Card combat
     * @property {string|null} [set] Card set
     * @property {Array.<string>|null} [attachments] Card attachments
     */

    /**
     * Constructs a new Card.
     * @exports Card
     * @classdesc Represents a Card.
     * @implements ICard
     * @constructor
     * @param {ICard=} [properties] Properties to set
     */
    function Card(properties) {
        this.counters = [];
        this.targets = [];
        this.knowledgeCost = [];
        this.types = [];
        this.subtypes = [];
        this.attachments = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Card id.
     * @member {string} id
     * @memberof Card
     * @instance
     */
    Card.prototype.id = "";

    /**
     * Card name.
     * @member {string} name
     * @memberof Card
     * @instance
     */
    Card.prototype.name = "";

    /**
     * Card counters.
     * @member {Array.<ICounterGroup>} counters
     * @memberof Card
     * @instance
     */
    Card.prototype.counters = $util.emptyArray;

    /**
     * Card depleted.
     * @member {boolean} depleted
     * @memberof Card
     * @instance
     */
    Card.prototype.depleted = false;

    /**
     * Card marked.
     * @member {boolean} marked
     * @memberof Card
     * @instance
     */
    Card.prototype.marked = false;

    /**
     * Card targets.
     * @member {Array.<string>} targets
     * @memberof Card
     * @instance
     */
    Card.prototype.targets = $util.emptyArray;

    /**
     * Card description.
     * @member {string} description
     * @memberof Card
     * @instance
     */
    Card.prototype.description = "";

    /**
     * Card cost.
     * @member {string} cost
     * @memberof Card
     * @instance
     */
    Card.prototype.cost = "";

    /**
     * Card knowledgeCost.
     * @member {Array.<IKnowledgeGroup>} knowledgeCost
     * @memberof Card
     * @instance
     */
    Card.prototype.knowledgeCost = $util.emptyArray;

    /**
     * Card types.
     * @member {Array.<string>} types
     * @memberof Card
     * @instance
     */
    Card.prototype.types = $util.emptyArray;

    /**
     * Card subtypes.
     * @member {Array.<string>} subtypes
     * @memberof Card
     * @instance
     */
    Card.prototype.subtypes = $util.emptyArray;

    /**
     * Card delay.
     * @member {number} delay
     * @memberof Card
     * @instance
     */
    Card.prototype.delay = 0;

    /**
     * Card loyalty.
     * @member {number} loyalty
     * @memberof Card
     * @instance
     */
    Card.prototype.loyalty = 0;

    /**
     * Card combat.
     * @member {ICombat|null|undefined} combat
     * @memberof Card
     * @instance
     */
    Card.prototype.combat = null;

    /**
     * Card set.
     * @member {string} set
     * @memberof Card
     * @instance
     */
    Card.prototype.set = "";

    /**
     * Card attachments.
     * @member {Array.<string>} attachments
     * @memberof Card
     * @instance
     */
    Card.prototype.attachments = $util.emptyArray;

    /**
     * Creates a new Card instance using the specified properties.
     * @function create
     * @memberof Card
     * @static
     * @param {ICard=} [properties] Properties to set
     * @returns {Card} Card instance
     */
    Card.create = function create(properties) {
        return new Card(properties);
    };

    /**
     * Encodes the specified Card message. Does not implicitly {@link Card.verify|verify} messages.
     * @function encode
     * @memberof Card
     * @static
     * @param {ICard} message Card message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Card.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.name != null && Object.hasOwnProperty.call(message, "name"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.name);
        if (message.counters != null && message.counters.length)
            for (var i = 0; i < message.counters.length; ++i)
                $root.CounterGroup.encode(message.counters[i], writer.uint32(/* id 3, wireType 2 =*/26).fork()).ldelim();
        if (message.depleted != null && Object.hasOwnProperty.call(message, "depleted"))
            writer.uint32(/* id 4, wireType 0 =*/32).bool(message.depleted);
        if (message.marked != null && Object.hasOwnProperty.call(message, "marked"))
            writer.uint32(/* id 5, wireType 0 =*/40).bool(message.marked);
        if (message.targets != null && message.targets.length)
            for (var i = 0; i < message.targets.length; ++i)
                writer.uint32(/* id 6, wireType 2 =*/50).string(message.targets[i]);
        if (message.description != null && Object.hasOwnProperty.call(message, "description"))
            writer.uint32(/* id 7, wireType 2 =*/58).string(message.description);
        if (message.cost != null && Object.hasOwnProperty.call(message, "cost"))
            writer.uint32(/* id 8, wireType 2 =*/66).string(message.cost);
        if (message.knowledgeCost != null && message.knowledgeCost.length)
            for (var i = 0; i < message.knowledgeCost.length; ++i)
                $root.KnowledgeGroup.encode(message.knowledgeCost[i], writer.uint32(/* id 9, wireType 2 =*/74).fork()).ldelim();
        if (message.types != null && message.types.length)
            for (var i = 0; i < message.types.length; ++i)
                writer.uint32(/* id 10, wireType 2 =*/82).string(message.types[i]);
        if (message.subtypes != null && message.subtypes.length)
            for (var i = 0; i < message.subtypes.length; ++i)
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.subtypes[i]);
        if (message.delay != null && Object.hasOwnProperty.call(message, "delay"))
            writer.uint32(/* id 13, wireType 0 =*/104).int32(message.delay);
        if (message.loyalty != null && Object.hasOwnProperty.call(message, "loyalty"))
            writer.uint32(/* id 14, wireType 0 =*/112).int32(message.loyalty);
        if (message.combat != null && Object.hasOwnProperty.call(message, "combat"))
            $root.Combat.encode(message.combat, writer.uint32(/* id 16, wireType 2 =*/130).fork()).ldelim();
        if (message.set != null && Object.hasOwnProperty.call(message, "set"))
            writer.uint32(/* id 17, wireType 2 =*/138).string(message.set);
        if (message.attachments != null && message.attachments.length)
            for (var i = 0; i < message.attachments.length; ++i)
                writer.uint32(/* id 18, wireType 2 =*/146).string(message.attachments[i]);
        return writer;
    };

    /**
     * Encodes the specified Card message, length delimited. Does not implicitly {@link Card.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Card
     * @static
     * @param {ICard} message Card message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Card.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Card message from the specified reader or buffer.
     * @function decode
     * @memberof Card
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Card} Card
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Card.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Card();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.id = reader.string();
                break;
            case 2:
                message.name = reader.string();
                break;
            case 3:
                if (!(message.counters && message.counters.length))
                    message.counters = [];
                message.counters.push($root.CounterGroup.decode(reader, reader.uint32()));
                break;
            case 4:
                message.depleted = reader.bool();
                break;
            case 5:
                message.marked = reader.bool();
                break;
            case 6:
                if (!(message.targets && message.targets.length))
                    message.targets = [];
                message.targets.push(reader.string());
                break;
            case 7:
                message.description = reader.string();
                break;
            case 8:
                message.cost = reader.string();
                break;
            case 9:
                if (!(message.knowledgeCost && message.knowledgeCost.length))
                    message.knowledgeCost = [];
                message.knowledgeCost.push($root.KnowledgeGroup.decode(reader, reader.uint32()));
                break;
            case 10:
                if (!(message.types && message.types.length))
                    message.types = [];
                message.types.push(reader.string());
                break;
            case 11:
                if (!(message.subtypes && message.subtypes.length))
                    message.subtypes = [];
                message.subtypes.push(reader.string());
                break;
            case 13:
                message.delay = reader.int32();
                break;
            case 14:
                message.loyalty = reader.int32();
                break;
            case 16:
                message.combat = $root.Combat.decode(reader, reader.uint32());
                break;
            case 17:
                message.set = reader.string();
                break;
            case 18:
                if (!(message.attachments && message.attachments.length))
                    message.attachments = [];
                message.attachments.push(reader.string());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Card message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Card
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Card} Card
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Card.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Card message.
     * @function verify
     * @memberof Card
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Card.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.name != null && message.hasOwnProperty("name"))
            if (!$util.isString(message.name))
                return "name: string expected";
        if (message.counters != null && message.hasOwnProperty("counters")) {
            if (!Array.isArray(message.counters))
                return "counters: array expected";
            for (var i = 0; i < message.counters.length; ++i) {
                var error = $root.CounterGroup.verify(message.counters[i]);
                if (error)
                    return "counters." + error;
            }
        }
        if (message.depleted != null && message.hasOwnProperty("depleted"))
            if (typeof message.depleted !== "boolean")
                return "depleted: boolean expected";
        if (message.marked != null && message.hasOwnProperty("marked"))
            if (typeof message.marked !== "boolean")
                return "marked: boolean expected";
        if (message.targets != null && message.hasOwnProperty("targets")) {
            if (!Array.isArray(message.targets))
                return "targets: array expected";
            for (var i = 0; i < message.targets.length; ++i)
                if (!$util.isString(message.targets[i]))
                    return "targets: string[] expected";
        }
        if (message.description != null && message.hasOwnProperty("description"))
            if (!$util.isString(message.description))
                return "description: string expected";
        if (message.cost != null && message.hasOwnProperty("cost"))
            if (!$util.isString(message.cost))
                return "cost: string expected";
        if (message.knowledgeCost != null && message.hasOwnProperty("knowledgeCost")) {
            if (!Array.isArray(message.knowledgeCost))
                return "knowledgeCost: array expected";
            for (var i = 0; i < message.knowledgeCost.length; ++i) {
                var error = $root.KnowledgeGroup.verify(message.knowledgeCost[i]);
                if (error)
                    return "knowledgeCost." + error;
            }
        }
        if (message.types != null && message.hasOwnProperty("types")) {
            if (!Array.isArray(message.types))
                return "types: array expected";
            for (var i = 0; i < message.types.length; ++i)
                if (!$util.isString(message.types[i]))
                    return "types: string[] expected";
        }
        if (message.subtypes != null && message.hasOwnProperty("subtypes")) {
            if (!Array.isArray(message.subtypes))
                return "subtypes: array expected";
            for (var i = 0; i < message.subtypes.length; ++i)
                if (!$util.isString(message.subtypes[i]))
                    return "subtypes: string[] expected";
        }
        if (message.delay != null && message.hasOwnProperty("delay"))
            if (!$util.isInteger(message.delay))
                return "delay: integer expected";
        if (message.loyalty != null && message.hasOwnProperty("loyalty"))
            if (!$util.isInteger(message.loyalty))
                return "loyalty: integer expected";
        if (message.combat != null && message.hasOwnProperty("combat")) {
            var error = $root.Combat.verify(message.combat);
            if (error)
                return "combat." + error;
        }
        if (message.set != null && message.hasOwnProperty("set"))
            if (!$util.isString(message.set))
                return "set: string expected";
        if (message.attachments != null && message.hasOwnProperty("attachments")) {
            if (!Array.isArray(message.attachments))
                return "attachments: array expected";
            for (var i = 0; i < message.attachments.length; ++i)
                if (!$util.isString(message.attachments[i]))
                    return "attachments: string[] expected";
        }
        return null;
    };

    /**
     * Creates a Card message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Card
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Card} Card
     */
    Card.fromObject = function fromObject(object) {
        if (object instanceof $root.Card)
            return object;
        var message = new $root.Card();
        if (object.id != null)
            message.id = String(object.id);
        if (object.name != null)
            message.name = String(object.name);
        if (object.counters) {
            if (!Array.isArray(object.counters))
                throw TypeError(".Card.counters: array expected");
            message.counters = [];
            for (var i = 0; i < object.counters.length; ++i) {
                if (typeof object.counters[i] !== "object")
                    throw TypeError(".Card.counters: object expected");
                message.counters[i] = $root.CounterGroup.fromObject(object.counters[i]);
            }
        }
        if (object.depleted != null)
            message.depleted = Boolean(object.depleted);
        if (object.marked != null)
            message.marked = Boolean(object.marked);
        if (object.targets) {
            if (!Array.isArray(object.targets))
                throw TypeError(".Card.targets: array expected");
            message.targets = [];
            for (var i = 0; i < object.targets.length; ++i)
                message.targets[i] = String(object.targets[i]);
        }
        if (object.description != null)
            message.description = String(object.description);
        if (object.cost != null)
            message.cost = String(object.cost);
        if (object.knowledgeCost) {
            if (!Array.isArray(object.knowledgeCost))
                throw TypeError(".Card.knowledgeCost: array expected");
            message.knowledgeCost = [];
            for (var i = 0; i < object.knowledgeCost.length; ++i) {
                if (typeof object.knowledgeCost[i] !== "object")
                    throw TypeError(".Card.knowledgeCost: object expected");
                message.knowledgeCost[i] = $root.KnowledgeGroup.fromObject(object.knowledgeCost[i]);
            }
        }
        if (object.types) {
            if (!Array.isArray(object.types))
                throw TypeError(".Card.types: array expected");
            message.types = [];
            for (var i = 0; i < object.types.length; ++i)
                message.types[i] = String(object.types[i]);
        }
        if (object.subtypes) {
            if (!Array.isArray(object.subtypes))
                throw TypeError(".Card.subtypes: array expected");
            message.subtypes = [];
            for (var i = 0; i < object.subtypes.length; ++i)
                message.subtypes[i] = String(object.subtypes[i]);
        }
        if (object.delay != null)
            message.delay = object.delay | 0;
        if (object.loyalty != null)
            message.loyalty = object.loyalty | 0;
        if (object.combat != null) {
            if (typeof object.combat !== "object")
                throw TypeError(".Card.combat: object expected");
            message.combat = $root.Combat.fromObject(object.combat);
        }
        if (object.set != null)
            message.set = String(object.set);
        if (object.attachments) {
            if (!Array.isArray(object.attachments))
                throw TypeError(".Card.attachments: array expected");
            message.attachments = [];
            for (var i = 0; i < object.attachments.length; ++i)
                message.attachments[i] = String(object.attachments[i]);
        }
        return message;
    };

    /**
     * Creates a plain object from a Card message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Card
     * @static
     * @param {Card} message Card
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Card.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults) {
            object.counters = [];
            object.targets = [];
            object.knowledgeCost = [];
            object.types = [];
            object.subtypes = [];
            object.attachments = [];
        }
        if (options.defaults) {
            object.id = "";
            object.name = "";
            object.depleted = false;
            object.marked = false;
            object.description = "";
            object.cost = "";
            object.delay = 0;
            object.loyalty = 0;
            object.combat = null;
            object.set = "";
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.name != null && message.hasOwnProperty("name"))
            object.name = message.name;
        if (message.counters && message.counters.length) {
            object.counters = [];
            for (var j = 0; j < message.counters.length; ++j)
                object.counters[j] = $root.CounterGroup.toObject(message.counters[j], options);
        }
        if (message.depleted != null && message.hasOwnProperty("depleted"))
            object.depleted = message.depleted;
        if (message.marked != null && message.hasOwnProperty("marked"))
            object.marked = message.marked;
        if (message.targets && message.targets.length) {
            object.targets = [];
            for (var j = 0; j < message.targets.length; ++j)
                object.targets[j] = message.targets[j];
        }
        if (message.description != null && message.hasOwnProperty("description"))
            object.description = message.description;
        if (message.cost != null && message.hasOwnProperty("cost"))
            object.cost = message.cost;
        if (message.knowledgeCost && message.knowledgeCost.length) {
            object.knowledgeCost = [];
            for (var j = 0; j < message.knowledgeCost.length; ++j)
                object.knowledgeCost[j] = $root.KnowledgeGroup.toObject(message.knowledgeCost[j], options);
        }
        if (message.types && message.types.length) {
            object.types = [];
            for (var j = 0; j < message.types.length; ++j)
                object.types[j] = message.types[j];
        }
        if (message.subtypes && message.subtypes.length) {
            object.subtypes = [];
            for (var j = 0; j < message.subtypes.length; ++j)
                object.subtypes[j] = message.subtypes[j];
        }
        if (message.delay != null && message.hasOwnProperty("delay"))
            object.delay = message.delay;
        if (message.loyalty != null && message.hasOwnProperty("loyalty"))
            object.loyalty = message.loyalty;
        if (message.combat != null && message.hasOwnProperty("combat"))
            object.combat = $root.Combat.toObject(message.combat, options);
        if (message.set != null && message.hasOwnProperty("set"))
            object.set = message.set;
        if (message.attachments && message.attachments.length) {
            object.attachments = [];
            for (var j = 0; j < message.attachments.length; ++j)
                object.attachments[j] = message.attachments[j];
        }
        return object;
    };

    /**
     * Converts this Card to JSON.
     * @function toJSON
     * @memberof Card
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Card.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Card;
})();

$root.Player = (function() {

    /**
     * Properties of a Player.
     * @exports IPlayer
     * @interface IPlayer
     * @property {string|null} [id] Player id
     * @property {string|null} [username] Player username
     * @property {number|null} [deckSize] Player deckSize
     * @property {number|null} [energy] Player energy
     * @property {number|null} [maxEnergy] Player maxEnergy
     * @property {Array.<ICard>|null} [hand] Player hand
     * @property {Array.<ICard>|null} [play] Player play
     * @property {Array.<ICard>|null} [discardPile] Player discardPile
     * @property {Array.<ICard>|null} ["void"] Player void
     * @property {Array.<IKnowledgeGroup>|null} [knowledgePool] Player knowledgePool
     * @property {number|null} [shield] Player shield
     * @property {number|null} [handSize] Player handSize
     * @property {number|null} [health] Player health
     * @property {number|null} [time] Player time
     * @property {Array.<IKnowledgeGroup>|null} [deckColors] Player deckColors
     */

    /**
     * Constructs a new Player.
     * @exports Player
     * @classdesc Represents a Player.
     * @implements IPlayer
     * @constructor
     * @param {IPlayer=} [properties] Properties to set
     */
    function Player(properties) {
        this.hand = [];
        this.play = [];
        this.discardPile = [];
        this["void"] = [];
        this.knowledgePool = [];
        this.deckColors = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * Player id.
     * @member {string} id
     * @memberof Player
     * @instance
     */
    Player.prototype.id = "";

    /**
     * Player username.
     * @member {string} username
     * @memberof Player
     * @instance
     */
    Player.prototype.username = "";

    /**
     * Player deckSize.
     * @member {number} deckSize
     * @memberof Player
     * @instance
     */
    Player.prototype.deckSize = 0;

    /**
     * Player energy.
     * @member {number} energy
     * @memberof Player
     * @instance
     */
    Player.prototype.energy = 0;

    /**
     * Player maxEnergy.
     * @member {number} maxEnergy
     * @memberof Player
     * @instance
     */
    Player.prototype.maxEnergy = 0;

    /**
     * Player hand.
     * @member {Array.<ICard>} hand
     * @memberof Player
     * @instance
     */
    Player.prototype.hand = $util.emptyArray;

    /**
     * Player play.
     * @member {Array.<ICard>} play
     * @memberof Player
     * @instance
     */
    Player.prototype.play = $util.emptyArray;

    /**
     * Player discardPile.
     * @member {Array.<ICard>} discardPile
     * @memberof Player
     * @instance
     */
    Player.prototype.discardPile = $util.emptyArray;

    /**
     * Player void.
     * @member {Array.<ICard>} void
     * @memberof Player
     * @instance
     */
    Player.prototype["void"] = $util.emptyArray;

    /**
     * Player knowledgePool.
     * @member {Array.<IKnowledgeGroup>} knowledgePool
     * @memberof Player
     * @instance
     */
    Player.prototype.knowledgePool = $util.emptyArray;

    /**
     * Player shield.
     * @member {number} shield
     * @memberof Player
     * @instance
     */
    Player.prototype.shield = 0;

    /**
     * Player handSize.
     * @member {number} handSize
     * @memberof Player
     * @instance
     */
    Player.prototype.handSize = 0;

    /**
     * Player health.
     * @member {number} health
     * @memberof Player
     * @instance
     */
    Player.prototype.health = 0;

    /**
     * Player time.
     * @member {number} time
     * @memberof Player
     * @instance
     */
    Player.prototype.time = 0;

    /**
     * Player deckColors.
     * @member {Array.<IKnowledgeGroup>} deckColors
     * @memberof Player
     * @instance
     */
    Player.prototype.deckColors = $util.emptyArray;

    /**
     * Creates a new Player instance using the specified properties.
     * @function create
     * @memberof Player
     * @static
     * @param {IPlayer=} [properties] Properties to set
     * @returns {Player} Player instance
     */
    Player.create = function create(properties) {
        return new Player(properties);
    };

    /**
     * Encodes the specified Player message. Does not implicitly {@link Player.verify|verify} messages.
     * @function encode
     * @memberof Player
     * @static
     * @param {IPlayer} message Player message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Player.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.username != null && Object.hasOwnProperty.call(message, "username"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.username);
        if (message.deckSize != null && Object.hasOwnProperty.call(message, "deckSize"))
            writer.uint32(/* id 3, wireType 0 =*/24).int32(message.deckSize);
        if (message.energy != null && Object.hasOwnProperty.call(message, "energy"))
            writer.uint32(/* id 4, wireType 0 =*/32).int32(message.energy);
        if (message.maxEnergy != null && Object.hasOwnProperty.call(message, "maxEnergy"))
            writer.uint32(/* id 5, wireType 0 =*/40).int32(message.maxEnergy);
        if (message.hand != null && message.hand.length)
            for (var i = 0; i < message.hand.length; ++i)
                $root.Card.encode(message.hand[i], writer.uint32(/* id 6, wireType 2 =*/50).fork()).ldelim();
        if (message.play != null && message.play.length)
            for (var i = 0; i < message.play.length; ++i)
                $root.Card.encode(message.play[i], writer.uint32(/* id 7, wireType 2 =*/58).fork()).ldelim();
        if (message.discardPile != null && message.discardPile.length)
            for (var i = 0; i < message.discardPile.length; ++i)
                $root.Card.encode(message.discardPile[i], writer.uint32(/* id 8, wireType 2 =*/66).fork()).ldelim();
        if (message["void"] != null && message["void"].length)
            for (var i = 0; i < message["void"].length; ++i)
                $root.Card.encode(message["void"][i], writer.uint32(/* id 9, wireType 2 =*/74).fork()).ldelim();
        if (message.knowledgePool != null && message.knowledgePool.length)
            for (var i = 0; i < message.knowledgePool.length; ++i)
                $root.KnowledgeGroup.encode(message.knowledgePool[i], writer.uint32(/* id 10, wireType 2 =*/82).fork()).ldelim();
        if (message.shield != null && Object.hasOwnProperty.call(message, "shield"))
            writer.uint32(/* id 12, wireType 0 =*/96).int32(message.shield);
        if (message.handSize != null && Object.hasOwnProperty.call(message, "handSize"))
            writer.uint32(/* id 13, wireType 0 =*/104).int32(message.handSize);
        if (message.health != null && Object.hasOwnProperty.call(message, "health"))
            writer.uint32(/* id 14, wireType 0 =*/112).int32(message.health);
        if (message.time != null && Object.hasOwnProperty.call(message, "time"))
            writer.uint32(/* id 15, wireType 0 =*/120).int32(message.time);
        if (message.deckColors != null && message.deckColors.length)
            for (var i = 0; i < message.deckColors.length; ++i)
                $root.KnowledgeGroup.encode(message.deckColors[i], writer.uint32(/* id 16, wireType 2 =*/130).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified Player message, length delimited. Does not implicitly {@link Player.verify|verify} messages.
     * @function encodeDelimited
     * @memberof Player
     * @static
     * @param {IPlayer} message Player message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    Player.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a Player message from the specified reader or buffer.
     * @function decode
     * @memberof Player
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {Player} Player
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Player.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.Player();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.id = reader.string();
                break;
            case 2:
                message.username = reader.string();
                break;
            case 3:
                message.deckSize = reader.int32();
                break;
            case 4:
                message.energy = reader.int32();
                break;
            case 5:
                message.maxEnergy = reader.int32();
                break;
            case 6:
                if (!(message.hand && message.hand.length))
                    message.hand = [];
                message.hand.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 7:
                if (!(message.play && message.play.length))
                    message.play = [];
                message.play.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 8:
                if (!(message.discardPile && message.discardPile.length))
                    message.discardPile = [];
                message.discardPile.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 9:
                if (!(message["void"] && message["void"].length))
                    message["void"] = [];
                message["void"].push($root.Card.decode(reader, reader.uint32()));
                break;
            case 10:
                if (!(message.knowledgePool && message.knowledgePool.length))
                    message.knowledgePool = [];
                message.knowledgePool.push($root.KnowledgeGroup.decode(reader, reader.uint32()));
                break;
            case 12:
                message.shield = reader.int32();
                break;
            case 13:
                message.handSize = reader.int32();
                break;
            case 14:
                message.health = reader.int32();
                break;
            case 15:
                message.time = reader.int32();
                break;
            case 16:
                if (!(message.deckColors && message.deckColors.length))
                    message.deckColors = [];
                message.deckColors.push($root.KnowledgeGroup.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a Player message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof Player
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {Player} Player
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    Player.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a Player message.
     * @function verify
     * @memberof Player
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    Player.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.username != null && message.hasOwnProperty("username"))
            if (!$util.isString(message.username))
                return "username: string expected";
        if (message.deckSize != null && message.hasOwnProperty("deckSize"))
            if (!$util.isInteger(message.deckSize))
                return "deckSize: integer expected";
        if (message.energy != null && message.hasOwnProperty("energy"))
            if (!$util.isInteger(message.energy))
                return "energy: integer expected";
        if (message.maxEnergy != null && message.hasOwnProperty("maxEnergy"))
            if (!$util.isInteger(message.maxEnergy))
                return "maxEnergy: integer expected";
        if (message.hand != null && message.hasOwnProperty("hand")) {
            if (!Array.isArray(message.hand))
                return "hand: array expected";
            for (var i = 0; i < message.hand.length; ++i) {
                var error = $root.Card.verify(message.hand[i]);
                if (error)
                    return "hand." + error;
            }
        }
        if (message.play != null && message.hasOwnProperty("play")) {
            if (!Array.isArray(message.play))
                return "play: array expected";
            for (var i = 0; i < message.play.length; ++i) {
                var error = $root.Card.verify(message.play[i]);
                if (error)
                    return "play." + error;
            }
        }
        if (message.discardPile != null && message.hasOwnProperty("discardPile")) {
            if (!Array.isArray(message.discardPile))
                return "discardPile: array expected";
            for (var i = 0; i < message.discardPile.length; ++i) {
                var error = $root.Card.verify(message.discardPile[i]);
                if (error)
                    return "discardPile." + error;
            }
        }
        if (message["void"] != null && message.hasOwnProperty("void")) {
            if (!Array.isArray(message["void"]))
                return "void: array expected";
            for (var i = 0; i < message["void"].length; ++i) {
                var error = $root.Card.verify(message["void"][i]);
                if (error)
                    return "void." + error;
            }
        }
        if (message.knowledgePool != null && message.hasOwnProperty("knowledgePool")) {
            if (!Array.isArray(message.knowledgePool))
                return "knowledgePool: array expected";
            for (var i = 0; i < message.knowledgePool.length; ++i) {
                var error = $root.KnowledgeGroup.verify(message.knowledgePool[i]);
                if (error)
                    return "knowledgePool." + error;
            }
        }
        if (message.shield != null && message.hasOwnProperty("shield"))
            if (!$util.isInteger(message.shield))
                return "shield: integer expected";
        if (message.handSize != null && message.hasOwnProperty("handSize"))
            if (!$util.isInteger(message.handSize))
                return "handSize: integer expected";
        if (message.health != null && message.hasOwnProperty("health"))
            if (!$util.isInteger(message.health))
                return "health: integer expected";
        if (message.time != null && message.hasOwnProperty("time"))
            if (!$util.isInteger(message.time))
                return "time: integer expected";
        if (message.deckColors != null && message.hasOwnProperty("deckColors")) {
            if (!Array.isArray(message.deckColors))
                return "deckColors: array expected";
            for (var i = 0; i < message.deckColors.length; ++i) {
                var error = $root.KnowledgeGroup.verify(message.deckColors[i]);
                if (error)
                    return "deckColors." + error;
            }
        }
        return null;
    };

    /**
     * Creates a Player message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof Player
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {Player} Player
     */
    Player.fromObject = function fromObject(object) {
        if (object instanceof $root.Player)
            return object;
        var message = new $root.Player();
        if (object.id != null)
            message.id = String(object.id);
        if (object.username != null)
            message.username = String(object.username);
        if (object.deckSize != null)
            message.deckSize = object.deckSize | 0;
        if (object.energy != null)
            message.energy = object.energy | 0;
        if (object.maxEnergy != null)
            message.maxEnergy = object.maxEnergy | 0;
        if (object.hand) {
            if (!Array.isArray(object.hand))
                throw TypeError(".Player.hand: array expected");
            message.hand = [];
            for (var i = 0; i < object.hand.length; ++i) {
                if (typeof object.hand[i] !== "object")
                    throw TypeError(".Player.hand: object expected");
                message.hand[i] = $root.Card.fromObject(object.hand[i]);
            }
        }
        if (object.play) {
            if (!Array.isArray(object.play))
                throw TypeError(".Player.play: array expected");
            message.play = [];
            for (var i = 0; i < object.play.length; ++i) {
                if (typeof object.play[i] !== "object")
                    throw TypeError(".Player.play: object expected");
                message.play[i] = $root.Card.fromObject(object.play[i]);
            }
        }
        if (object.discardPile) {
            if (!Array.isArray(object.discardPile))
                throw TypeError(".Player.discardPile: array expected");
            message.discardPile = [];
            for (var i = 0; i < object.discardPile.length; ++i) {
                if (typeof object.discardPile[i] !== "object")
                    throw TypeError(".Player.discardPile: object expected");
                message.discardPile[i] = $root.Card.fromObject(object.discardPile[i]);
            }
        }
        if (object["void"]) {
            if (!Array.isArray(object["void"]))
                throw TypeError(".Player.void: array expected");
            message["void"] = [];
            for (var i = 0; i < object["void"].length; ++i) {
                if (typeof object["void"][i] !== "object")
                    throw TypeError(".Player.void: object expected");
                message["void"][i] = $root.Card.fromObject(object["void"][i]);
            }
        }
        if (object.knowledgePool) {
            if (!Array.isArray(object.knowledgePool))
                throw TypeError(".Player.knowledgePool: array expected");
            message.knowledgePool = [];
            for (var i = 0; i < object.knowledgePool.length; ++i) {
                if (typeof object.knowledgePool[i] !== "object")
                    throw TypeError(".Player.knowledgePool: object expected");
                message.knowledgePool[i] = $root.KnowledgeGroup.fromObject(object.knowledgePool[i]);
            }
        }
        if (object.shield != null)
            message.shield = object.shield | 0;
        if (object.handSize != null)
            message.handSize = object.handSize | 0;
        if (object.health != null)
            message.health = object.health | 0;
        if (object.time != null)
            message.time = object.time | 0;
        if (object.deckColors) {
            if (!Array.isArray(object.deckColors))
                throw TypeError(".Player.deckColors: array expected");
            message.deckColors = [];
            for (var i = 0; i < object.deckColors.length; ++i) {
                if (typeof object.deckColors[i] !== "object")
                    throw TypeError(".Player.deckColors: object expected");
                message.deckColors[i] = $root.KnowledgeGroup.fromObject(object.deckColors[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a Player message. Also converts values to other types if specified.
     * @function toObject
     * @memberof Player
     * @static
     * @param {Player} message Player
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    Player.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults) {
            object.hand = [];
            object.play = [];
            object.discardPile = [];
            object["void"] = [];
            object.knowledgePool = [];
            object.deckColors = [];
        }
        if (options.defaults) {
            object.id = "";
            object.username = "";
            object.deckSize = 0;
            object.energy = 0;
            object.maxEnergy = 0;
            object.shield = 0;
            object.handSize = 0;
            object.health = 0;
            object.time = 0;
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.username != null && message.hasOwnProperty("username"))
            object.username = message.username;
        if (message.deckSize != null && message.hasOwnProperty("deckSize"))
            object.deckSize = message.deckSize;
        if (message.energy != null && message.hasOwnProperty("energy"))
            object.energy = message.energy;
        if (message.maxEnergy != null && message.hasOwnProperty("maxEnergy"))
            object.maxEnergy = message.maxEnergy;
        if (message.hand && message.hand.length) {
            object.hand = [];
            for (var j = 0; j < message.hand.length; ++j)
                object.hand[j] = $root.Card.toObject(message.hand[j], options);
        }
        if (message.play && message.play.length) {
            object.play = [];
            for (var j = 0; j < message.play.length; ++j)
                object.play[j] = $root.Card.toObject(message.play[j], options);
        }
        if (message.discardPile && message.discardPile.length) {
            object.discardPile = [];
            for (var j = 0; j < message.discardPile.length; ++j)
                object.discardPile[j] = $root.Card.toObject(message.discardPile[j], options);
        }
        if (message["void"] && message["void"].length) {
            object["void"] = [];
            for (var j = 0; j < message["void"].length; ++j)
                object["void"][j] = $root.Card.toObject(message["void"][j], options);
        }
        if (message.knowledgePool && message.knowledgePool.length) {
            object.knowledgePool = [];
            for (var j = 0; j < message.knowledgePool.length; ++j)
                object.knowledgePool[j] = $root.KnowledgeGroup.toObject(message.knowledgePool[j], options);
        }
        if (message.shield != null && message.hasOwnProperty("shield"))
            object.shield = message.shield;
        if (message.handSize != null && message.hasOwnProperty("handSize"))
            object.handSize = message.handSize;
        if (message.health != null && message.hasOwnProperty("health"))
            object.health = message.health;
        if (message.time != null && message.hasOwnProperty("time"))
            object.time = message.time;
        if (message.deckColors && message.deckColors.length) {
            object.deckColors = [];
            for (var j = 0; j < message.deckColors.length; ++j)
                object.deckColors[j] = $root.KnowledgeGroup.toObject(message.deckColors[j], options);
        }
        return object;
    };

    /**
     * Converts this Player to JSON.
     * @function toJSON
     * @memberof Player
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    Player.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return Player;
})();

$root.GameState = (function() {

    /**
     * Properties of a GameState.
     * @exports IGameState
     * @interface IGameState
     * @property {string|null} [id] GameState id
     * @property {IPlayer|null} [player] GameState player
     * @property {IPlayer|null} [opponent] GameState opponent
     * @property {string|null} [turnPlayer] GameState turnPlayer
     * @property {string|null} [activePlayer] GameState activePlayer
     * @property {Array.<ICard>|null} [stack] GameState stack
     * @property {Array.<string>|null} [canPlay] GameState canPlay
     * @property {Array.<string>|null} [canActivate] GameState canActivate
     * @property {Array.<string>|null} [canStudy] GameState canStudy
     * @property {Array.<string>|null} [attackers] GameState attackers
     * @property {Array.<string>|null} [blockers] GameState blockers
     * @property {Phase|null} [phase] GameState phase
     */

    /**
     * Constructs a new GameState.
     * @exports GameState
     * @classdesc Represents a GameState.
     * @implements IGameState
     * @constructor
     * @param {IGameState=} [properties] Properties to set
     */
    function GameState(properties) {
        this.stack = [];
        this.canPlay = [];
        this.canActivate = [];
        this.canStudy = [];
        this.attackers = [];
        this.blockers = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * GameState id.
     * @member {string} id
     * @memberof GameState
     * @instance
     */
    GameState.prototype.id = "";

    /**
     * GameState player.
     * @member {IPlayer|null|undefined} player
     * @memberof GameState
     * @instance
     */
    GameState.prototype.player = null;

    /**
     * GameState opponent.
     * @member {IPlayer|null|undefined} opponent
     * @memberof GameState
     * @instance
     */
    GameState.prototype.opponent = null;

    /**
     * GameState turnPlayer.
     * @member {string} turnPlayer
     * @memberof GameState
     * @instance
     */
    GameState.prototype.turnPlayer = "";

    /**
     * GameState activePlayer.
     * @member {string} activePlayer
     * @memberof GameState
     * @instance
     */
    GameState.prototype.activePlayer = "";

    /**
     * GameState stack.
     * @member {Array.<ICard>} stack
     * @memberof GameState
     * @instance
     */
    GameState.prototype.stack = $util.emptyArray;

    /**
     * GameState canPlay.
     * @member {Array.<string>} canPlay
     * @memberof GameState
     * @instance
     */
    GameState.prototype.canPlay = $util.emptyArray;

    /**
     * GameState canActivate.
     * @member {Array.<string>} canActivate
     * @memberof GameState
     * @instance
     */
    GameState.prototype.canActivate = $util.emptyArray;

    /**
     * GameState canStudy.
     * @member {Array.<string>} canStudy
     * @memberof GameState
     * @instance
     */
    GameState.prototype.canStudy = $util.emptyArray;

    /**
     * GameState attackers.
     * @member {Array.<string>} attackers
     * @memberof GameState
     * @instance
     */
    GameState.prototype.attackers = $util.emptyArray;

    /**
     * GameState blockers.
     * @member {Array.<string>} blockers
     * @memberof GameState
     * @instance
     */
    GameState.prototype.blockers = $util.emptyArray;

    /**
     * GameState phase.
     * @member {Phase} phase
     * @memberof GameState
     * @instance
     */
    GameState.prototype.phase = 0;

    /**
     * Creates a new GameState instance using the specified properties.
     * @function create
     * @memberof GameState
     * @static
     * @param {IGameState=} [properties] Properties to set
     * @returns {GameState} GameState instance
     */
    GameState.create = function create(properties) {
        return new GameState(properties);
    };

    /**
     * Encodes the specified GameState message. Does not implicitly {@link GameState.verify|verify} messages.
     * @function encode
     * @memberof GameState
     * @static
     * @param {IGameState} message GameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    GameState.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.player != null && Object.hasOwnProperty.call(message, "player"))
            $root.Player.encode(message.player, writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        if (message.opponent != null && Object.hasOwnProperty.call(message, "opponent"))
            $root.Player.encode(message.opponent, writer.uint32(/* id 3, wireType 2 =*/26).fork()).ldelim();
        if (message.turnPlayer != null && Object.hasOwnProperty.call(message, "turnPlayer"))
            writer.uint32(/* id 4, wireType 2 =*/34).string(message.turnPlayer);
        if (message.activePlayer != null && Object.hasOwnProperty.call(message, "activePlayer"))
            writer.uint32(/* id 5, wireType 2 =*/42).string(message.activePlayer);
        if (message.stack != null && message.stack.length)
            for (var i = 0; i < message.stack.length; ++i)
                $root.Card.encode(message.stack[i], writer.uint32(/* id 6, wireType 2 =*/50).fork()).ldelim();
        if (message.canPlay != null && message.canPlay.length)
            for (var i = 0; i < message.canPlay.length; ++i)
                writer.uint32(/* id 7, wireType 2 =*/58).string(message.canPlay[i]);
        if (message.canActivate != null && message.canActivate.length)
            for (var i = 0; i < message.canActivate.length; ++i)
                writer.uint32(/* id 8, wireType 2 =*/66).string(message.canActivate[i]);
        if (message.canStudy != null && message.canStudy.length)
            for (var i = 0; i < message.canStudy.length; ++i)
                writer.uint32(/* id 9, wireType 2 =*/74).string(message.canStudy[i]);
        if (message.phase != null && Object.hasOwnProperty.call(message, "phase"))
            writer.uint32(/* id 10, wireType 0 =*/80).int32(message.phase);
        if (message.attackers != null && message.attackers.length)
            for (var i = 0; i < message.attackers.length; ++i)
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.attackers[i]);
        if (message.blockers != null && message.blockers.length)
            for (var i = 0; i < message.blockers.length; ++i)
                writer.uint32(/* id 12, wireType 2 =*/98).string(message.blockers[i]);
        return writer;
    };

    /**
     * Encodes the specified GameState message, length delimited. Does not implicitly {@link GameState.verify|verify} messages.
     * @function encodeDelimited
     * @memberof GameState
     * @static
     * @param {IGameState} message GameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    GameState.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a GameState message from the specified reader or buffer.
     * @function decode
     * @memberof GameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {GameState} GameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    GameState.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.GameState();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.id = reader.string();
                break;
            case 2:
                message.player = $root.Player.decode(reader, reader.uint32());
                break;
            case 3:
                message.opponent = $root.Player.decode(reader, reader.uint32());
                break;
            case 4:
                message.turnPlayer = reader.string();
                break;
            case 5:
                message.activePlayer = reader.string();
                break;
            case 6:
                if (!(message.stack && message.stack.length))
                    message.stack = [];
                message.stack.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 7:
                if (!(message.canPlay && message.canPlay.length))
                    message.canPlay = [];
                message.canPlay.push(reader.string());
                break;
            case 8:
                if (!(message.canActivate && message.canActivate.length))
                    message.canActivate = [];
                message.canActivate.push(reader.string());
                break;
            case 9:
                if (!(message.canStudy && message.canStudy.length))
                    message.canStudy = [];
                message.canStudy.push(reader.string());
                break;
            case 11:
                if (!(message.attackers && message.attackers.length))
                    message.attackers = [];
                message.attackers.push(reader.string());
                break;
            case 12:
                if (!(message.blockers && message.blockers.length))
                    message.blockers = [];
                message.blockers.push(reader.string());
                break;
            case 10:
                message.phase = reader.int32();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a GameState message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof GameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {GameState} GameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    GameState.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a GameState message.
     * @function verify
     * @memberof GameState
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    GameState.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.player != null && message.hasOwnProperty("player")) {
            var error = $root.Player.verify(message.player);
            if (error)
                return "player." + error;
        }
        if (message.opponent != null && message.hasOwnProperty("opponent")) {
            var error = $root.Player.verify(message.opponent);
            if (error)
                return "opponent." + error;
        }
        if (message.turnPlayer != null && message.hasOwnProperty("turnPlayer"))
            if (!$util.isString(message.turnPlayer))
                return "turnPlayer: string expected";
        if (message.activePlayer != null && message.hasOwnProperty("activePlayer"))
            if (!$util.isString(message.activePlayer))
                return "activePlayer: string expected";
        if (message.stack != null && message.hasOwnProperty("stack")) {
            if (!Array.isArray(message.stack))
                return "stack: array expected";
            for (var i = 0; i < message.stack.length; ++i) {
                var error = $root.Card.verify(message.stack[i]);
                if (error)
                    return "stack." + error;
            }
        }
        if (message.canPlay != null && message.hasOwnProperty("canPlay")) {
            if (!Array.isArray(message.canPlay))
                return "canPlay: array expected";
            for (var i = 0; i < message.canPlay.length; ++i)
                if (!$util.isString(message.canPlay[i]))
                    return "canPlay: string[] expected";
        }
        if (message.canActivate != null && message.hasOwnProperty("canActivate")) {
            if (!Array.isArray(message.canActivate))
                return "canActivate: array expected";
            for (var i = 0; i < message.canActivate.length; ++i)
                if (!$util.isString(message.canActivate[i]))
                    return "canActivate: string[] expected";
        }
        if (message.canStudy != null && message.hasOwnProperty("canStudy")) {
            if (!Array.isArray(message.canStudy))
                return "canStudy: array expected";
            for (var i = 0; i < message.canStudy.length; ++i)
                if (!$util.isString(message.canStudy[i]))
                    return "canStudy: string[] expected";
        }
        if (message.attackers != null && message.hasOwnProperty("attackers")) {
            if (!Array.isArray(message.attackers))
                return "attackers: array expected";
            for (var i = 0; i < message.attackers.length; ++i)
                if (!$util.isString(message.attackers[i]))
                    return "attackers: string[] expected";
        }
        if (message.blockers != null && message.hasOwnProperty("blockers")) {
            if (!Array.isArray(message.blockers))
                return "blockers: array expected";
            for (var i = 0; i < message.blockers.length; ++i)
                if (!$util.isString(message.blockers[i]))
                    return "blockers: string[] expected";
        }
        if (message.phase != null && message.hasOwnProperty("phase"))
            switch (message.phase) {
            default:
                return "phase: enum value expected";
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            }
        return null;
    };

    /**
     * Creates a GameState message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof GameState
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {GameState} GameState
     */
    GameState.fromObject = function fromObject(object) {
        if (object instanceof $root.GameState)
            return object;
        var message = new $root.GameState();
        if (object.id != null)
            message.id = String(object.id);
        if (object.player != null) {
            if (typeof object.player !== "object")
                throw TypeError(".GameState.player: object expected");
            message.player = $root.Player.fromObject(object.player);
        }
        if (object.opponent != null) {
            if (typeof object.opponent !== "object")
                throw TypeError(".GameState.opponent: object expected");
            message.opponent = $root.Player.fromObject(object.opponent);
        }
        if (object.turnPlayer != null)
            message.turnPlayer = String(object.turnPlayer);
        if (object.activePlayer != null)
            message.activePlayer = String(object.activePlayer);
        if (object.stack) {
            if (!Array.isArray(object.stack))
                throw TypeError(".GameState.stack: array expected");
            message.stack = [];
            for (var i = 0; i < object.stack.length; ++i) {
                if (typeof object.stack[i] !== "object")
                    throw TypeError(".GameState.stack: object expected");
                message.stack[i] = $root.Card.fromObject(object.stack[i]);
            }
        }
        if (object.canPlay) {
            if (!Array.isArray(object.canPlay))
                throw TypeError(".GameState.canPlay: array expected");
            message.canPlay = [];
            for (var i = 0; i < object.canPlay.length; ++i)
                message.canPlay[i] = String(object.canPlay[i]);
        }
        if (object.canActivate) {
            if (!Array.isArray(object.canActivate))
                throw TypeError(".GameState.canActivate: array expected");
            message.canActivate = [];
            for (var i = 0; i < object.canActivate.length; ++i)
                message.canActivate[i] = String(object.canActivate[i]);
        }
        if (object.canStudy) {
            if (!Array.isArray(object.canStudy))
                throw TypeError(".GameState.canStudy: array expected");
            message.canStudy = [];
            for (var i = 0; i < object.canStudy.length; ++i)
                message.canStudy[i] = String(object.canStudy[i]);
        }
        if (object.attackers) {
            if (!Array.isArray(object.attackers))
                throw TypeError(".GameState.attackers: array expected");
            message.attackers = [];
            for (var i = 0; i < object.attackers.length; ++i)
                message.attackers[i] = String(object.attackers[i]);
        }
        if (object.blockers) {
            if (!Array.isArray(object.blockers))
                throw TypeError(".GameState.blockers: array expected");
            message.blockers = [];
            for (var i = 0; i < object.blockers.length; ++i)
                message.blockers[i] = String(object.blockers[i]);
        }
        switch (object.phase) {
        case "NOPHASE":
        case 0:
            message.phase = 0;
            break;
        case "REDRAW":
        case 1:
            message.phase = 1;
            break;
        case "BEGIN":
        case 2:
            message.phase = 2;
            break;
        case "MAIN_BEFORE":
        case 3:
            message.phase = 3;
            break;
        case "ATTACK":
        case 4:
            message.phase = 4;
            break;
        case "BLOCK":
        case 5:
            message.phase = 5;
            break;
        case "MAIN_AFTER":
        case 6:
            message.phase = 6;
            break;
        case "END":
        case 7:
            message.phase = 7;
            break;
        }
        return message;
    };

    /**
     * Creates a plain object from a GameState message. Also converts values to other types if specified.
     * @function toObject
     * @memberof GameState
     * @static
     * @param {GameState} message GameState
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    GameState.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults) {
            object.stack = [];
            object.canPlay = [];
            object.canActivate = [];
            object.canStudy = [];
            object.attackers = [];
            object.blockers = [];
        }
        if (options.defaults) {
            object.id = "";
            object.player = null;
            object.opponent = null;
            object.turnPlayer = "";
            object.activePlayer = "";
            object.phase = options.enums === String ? "NOPHASE" : 0;
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.player != null && message.hasOwnProperty("player"))
            object.player = $root.Player.toObject(message.player, options);
        if (message.opponent != null && message.hasOwnProperty("opponent"))
            object.opponent = $root.Player.toObject(message.opponent, options);
        if (message.turnPlayer != null && message.hasOwnProperty("turnPlayer"))
            object.turnPlayer = message.turnPlayer;
        if (message.activePlayer != null && message.hasOwnProperty("activePlayer"))
            object.activePlayer = message.activePlayer;
        if (message.stack && message.stack.length) {
            object.stack = [];
            for (var j = 0; j < message.stack.length; ++j)
                object.stack[j] = $root.Card.toObject(message.stack[j], options);
        }
        if (message.canPlay && message.canPlay.length) {
            object.canPlay = [];
            for (var j = 0; j < message.canPlay.length; ++j)
                object.canPlay[j] = message.canPlay[j];
        }
        if (message.canActivate && message.canActivate.length) {
            object.canActivate = [];
            for (var j = 0; j < message.canActivate.length; ++j)
                object.canActivate[j] = message.canActivate[j];
        }
        if (message.canStudy && message.canStudy.length) {
            object.canStudy = [];
            for (var j = 0; j < message.canStudy.length; ++j)
                object.canStudy[j] = message.canStudy[j];
        }
        if (message.phase != null && message.hasOwnProperty("phase"))
            object.phase = options.enums === String ? $root.Phase[message.phase] : message.phase;
        if (message.attackers && message.attackers.length) {
            object.attackers = [];
            for (var j = 0; j < message.attackers.length; ++j)
                object.attackers[j] = message.attackers[j];
        }
        if (message.blockers && message.blockers.length) {
            object.blockers = [];
            for (var j = 0; j < message.blockers.length; ++j)
                object.blockers[j] = message.blockers[j];
        }
        return object;
    };

    /**
     * Converts this GameState to JSON.
     * @function toJSON
     * @memberof GameState
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    GameState.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return GameState;
})();

$root.DraftState = (function() {

    /**
     * Properties of a DraftState.
     * @exports IDraftState
     * @interface IDraftState
     * @property {string|null} [id] DraftState id
     * @property {string|null} [playerId] DraftState playerId
     * @property {Array.<string>|null} [decklist] DraftState decklist
     * @property {boolean|null} [completed] DraftState completed
     */

    /**
     * Constructs a new DraftState.
     * @exports DraftState
     * @classdesc Represents a DraftState.
     * @implements IDraftState
     * @constructor
     * @param {IDraftState=} [properties] Properties to set
     */
    function DraftState(properties) {
        this.decklist = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * DraftState id.
     * @member {string} id
     * @memberof DraftState
     * @instance
     */
    DraftState.prototype.id = "";

    /**
     * DraftState playerId.
     * @member {string} playerId
     * @memberof DraftState
     * @instance
     */
    DraftState.prototype.playerId = "";

    /**
     * DraftState decklist.
     * @member {Array.<string>} decklist
     * @memberof DraftState
     * @instance
     */
    DraftState.prototype.decklist = $util.emptyArray;

    /**
     * DraftState completed.
     * @member {boolean} completed
     * @memberof DraftState
     * @instance
     */
    DraftState.prototype.completed = false;

    /**
     * Creates a new DraftState instance using the specified properties.
     * @function create
     * @memberof DraftState
     * @static
     * @param {IDraftState=} [properties] Properties to set
     * @returns {DraftState} DraftState instance
     */
    DraftState.create = function create(properties) {
        return new DraftState(properties);
    };

    /**
     * Encodes the specified DraftState message. Does not implicitly {@link DraftState.verify|verify} messages.
     * @function encode
     * @memberof DraftState
     * @static
     * @param {IDraftState} message DraftState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    DraftState.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.decklist != null && message.decklist.length)
            for (var i = 0; i < message.decklist.length; ++i)
                writer.uint32(/* id 2, wireType 2 =*/18).string(message.decklist[i]);
        if (message.completed != null && Object.hasOwnProperty.call(message, "completed"))
            writer.uint32(/* id 3, wireType 0 =*/24).bool(message.completed);
        if (message.playerId != null && Object.hasOwnProperty.call(message, "playerId"))
            writer.uint32(/* id 4, wireType 2 =*/34).string(message.playerId);
        return writer;
    };

    /**
     * Encodes the specified DraftState message, length delimited. Does not implicitly {@link DraftState.verify|verify} messages.
     * @function encodeDelimited
     * @memberof DraftState
     * @static
     * @param {IDraftState} message DraftState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    DraftState.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a DraftState message from the specified reader or buffer.
     * @function decode
     * @memberof DraftState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {DraftState} DraftState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    DraftState.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.DraftState();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.id = reader.string();
                break;
            case 4:
                message.playerId = reader.string();
                break;
            case 2:
                if (!(message.decklist && message.decklist.length))
                    message.decklist = [];
                message.decklist.push(reader.string());
                break;
            case 3:
                message.completed = reader.bool();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a DraftState message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof DraftState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {DraftState} DraftState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    DraftState.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a DraftState message.
     * @function verify
     * @memberof DraftState
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    DraftState.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.playerId != null && message.hasOwnProperty("playerId"))
            if (!$util.isString(message.playerId))
                return "playerId: string expected";
        if (message.decklist != null && message.hasOwnProperty("decklist")) {
            if (!Array.isArray(message.decklist))
                return "decklist: array expected";
            for (var i = 0; i < message.decklist.length; ++i)
                if (!$util.isString(message.decklist[i]))
                    return "decklist: string[] expected";
        }
        if (message.completed != null && message.hasOwnProperty("completed"))
            if (typeof message.completed !== "boolean")
                return "completed: boolean expected";
        return null;
    };

    /**
     * Creates a DraftState message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof DraftState
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {DraftState} DraftState
     */
    DraftState.fromObject = function fromObject(object) {
        if (object instanceof $root.DraftState)
            return object;
        var message = new $root.DraftState();
        if (object.id != null)
            message.id = String(object.id);
        if (object.playerId != null)
            message.playerId = String(object.playerId);
        if (object.decklist) {
            if (!Array.isArray(object.decklist))
                throw TypeError(".DraftState.decklist: array expected");
            message.decklist = [];
            for (var i = 0; i < object.decklist.length; ++i)
                message.decklist[i] = String(object.decklist[i]);
        }
        if (object.completed != null)
            message.completed = Boolean(object.completed);
        return message;
    };

    /**
     * Creates a plain object from a DraftState message. Also converts values to other types if specified.
     * @function toObject
     * @memberof DraftState
     * @static
     * @param {DraftState} message DraftState
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    DraftState.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.decklist = [];
        if (options.defaults) {
            object.id = "";
            object.completed = false;
            object.playerId = "";
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.decklist && message.decklist.length) {
            object.decklist = [];
            for (var j = 0; j < message.decklist.length; ++j)
                object.decklist[j] = message.decklist[j];
        }
        if (message.completed != null && message.hasOwnProperty("completed"))
            object.completed = message.completed;
        if (message.playerId != null && message.hasOwnProperty("playerId"))
            object.playerId = message.playerId;
        return object;
    };

    /**
     * Converts this DraftState to JSON.
     * @function toJSON
     * @memberof DraftState
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    DraftState.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return DraftState;
})();

$root.JoinQueue = (function() {

    /**
     * Properties of a JoinQueue.
     * @exports IJoinQueue
     * @interface IJoinQueue
     * @property {Array.<string>|null} [decklist] JoinQueue decklist
     * @property {GameType|null} [gameType] JoinQueue gameType
     * @property {string|null} [draftId] JoinQueue draftId
     */

    /**
     * Constructs a new JoinQueue.
     * @exports JoinQueue
     * @classdesc Represents a JoinQueue.
     * @implements IJoinQueue
     * @constructor
     * @param {IJoinQueue=} [properties] Properties to set
     */
    function JoinQueue(properties) {
        this.decklist = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * JoinQueue decklist.
     * @member {Array.<string>} decklist
     * @memberof JoinQueue
     * @instance
     */
    JoinQueue.prototype.decklist = $util.emptyArray;

    /**
     * JoinQueue gameType.
     * @member {GameType} gameType
     * @memberof JoinQueue
     * @instance
     */
    JoinQueue.prototype.gameType = 0;

    /**
     * JoinQueue draftId.
     * @member {string} draftId
     * @memberof JoinQueue
     * @instance
     */
    JoinQueue.prototype.draftId = "";

    /**
     * Creates a new JoinQueue instance using the specified properties.
     * @function create
     * @memberof JoinQueue
     * @static
     * @param {IJoinQueue=} [properties] Properties to set
     * @returns {JoinQueue} JoinQueue instance
     */
    JoinQueue.create = function create(properties) {
        return new JoinQueue(properties);
    };

    /**
     * Encodes the specified JoinQueue message. Does not implicitly {@link JoinQueue.verify|verify} messages.
     * @function encode
     * @memberof JoinQueue
     * @static
     * @param {IJoinQueue} message JoinQueue message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    JoinQueue.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.decklist != null && message.decklist.length)
            for (var i = 0; i < message.decklist.length; ++i)
                writer.uint32(/* id 3, wireType 2 =*/26).string(message.decklist[i]);
        if (message.gameType != null && Object.hasOwnProperty.call(message, "gameType"))
            writer.uint32(/* id 4, wireType 0 =*/32).int32(message.gameType);
        if (message.draftId != null && Object.hasOwnProperty.call(message, "draftId"))
            writer.uint32(/* id 5, wireType 2 =*/42).string(message.draftId);
        return writer;
    };

    /**
     * Encodes the specified JoinQueue message, length delimited. Does not implicitly {@link JoinQueue.verify|verify} messages.
     * @function encodeDelimited
     * @memberof JoinQueue
     * @static
     * @param {IJoinQueue} message JoinQueue message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    JoinQueue.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a JoinQueue message from the specified reader or buffer.
     * @function decode
     * @memberof JoinQueue
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {JoinQueue} JoinQueue
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    JoinQueue.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.JoinQueue();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 3:
                if (!(message.decklist && message.decklist.length))
                    message.decklist = [];
                message.decklist.push(reader.string());
                break;
            case 4:
                message.gameType = reader.int32();
                break;
            case 5:
                message.draftId = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a JoinQueue message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof JoinQueue
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {JoinQueue} JoinQueue
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    JoinQueue.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a JoinQueue message.
     * @function verify
     * @memberof JoinQueue
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    JoinQueue.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.decklist != null && message.hasOwnProperty("decklist")) {
            if (!Array.isArray(message.decklist))
                return "decklist: array expected";
            for (var i = 0; i < message.decklist.length; ++i)
                if (!$util.isString(message.decklist[i]))
                    return "decklist: string[] expected";
        }
        if (message.gameType != null && message.hasOwnProperty("gameType"))
            switch (message.gameType) {
            default:
                return "gameType: enum value expected";
            case 0:
            case 1:
            case 2:
            case 3:
                break;
            }
        if (message.draftId != null && message.hasOwnProperty("draftId"))
            if (!$util.isString(message.draftId))
                return "draftId: string expected";
        return null;
    };

    /**
     * Creates a JoinQueue message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof JoinQueue
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {JoinQueue} JoinQueue
     */
    JoinQueue.fromObject = function fromObject(object) {
        if (object instanceof $root.JoinQueue)
            return object;
        var message = new $root.JoinQueue();
        if (object.decklist) {
            if (!Array.isArray(object.decklist))
                throw TypeError(".JoinQueue.decklist: array expected");
            message.decklist = [];
            for (var i = 0; i < object.decklist.length; ++i)
                message.decklist[i] = String(object.decklist[i]);
        }
        switch (object.gameType) {
        case "NOGAME":
        case 0:
            message.gameType = 0;
            break;
        case "BO1_CONSTRUCTED":
        case 1:
            message.gameType = 1;
            break;
        case "P2_DRAFT":
        case 2:
            message.gameType = 2;
            break;
        case "P2_DRAFT_GAME":
        case 3:
            message.gameType = 3;
            break;
        }
        if (object.draftId != null)
            message.draftId = String(object.draftId);
        return message;
    };

    /**
     * Creates a plain object from a JoinQueue message. Also converts values to other types if specified.
     * @function toObject
     * @memberof JoinQueue
     * @static
     * @param {JoinQueue} message JoinQueue
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    JoinQueue.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.decklist = [];
        if (options.defaults) {
            object.gameType = options.enums === String ? "NOGAME" : 0;
            object.draftId = "";
        }
        if (message.decklist && message.decklist.length) {
            object.decklist = [];
            for (var j = 0; j < message.decklist.length; ++j)
                object.decklist[j] = message.decklist[j];
        }
        if (message.gameType != null && message.hasOwnProperty("gameType"))
            object.gameType = options.enums === String ? $root.GameType[message.gameType] : message.gameType;
        if (message.draftId != null && message.hasOwnProperty("draftId"))
            object.draftId = message.draftId;
        return object;
    };

    /**
     * Converts this JoinQueue to JSON.
     * @function toJSON
     * @memberof JoinQueue
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    JoinQueue.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return JoinQueue;
})();

$root.LoadGame = (function() {

    /**
     * Properties of a LoadGame.
     * @exports ILoadGame
     * @interface ILoadGame
     * @property {string|null} [filename] LoadGame filename
     */

    /**
     * Constructs a new LoadGame.
     * @exports LoadGame
     * @classdesc Represents a LoadGame.
     * @implements ILoadGame
     * @constructor
     * @param {ILoadGame=} [properties] Properties to set
     */
    function LoadGame(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * LoadGame filename.
     * @member {string} filename
     * @memberof LoadGame
     * @instance
     */
    LoadGame.prototype.filename = "";

    /**
     * Creates a new LoadGame instance using the specified properties.
     * @function create
     * @memberof LoadGame
     * @static
     * @param {ILoadGame=} [properties] Properties to set
     * @returns {LoadGame} LoadGame instance
     */
    LoadGame.create = function create(properties) {
        return new LoadGame(properties);
    };

    /**
     * Encodes the specified LoadGame message. Does not implicitly {@link LoadGame.verify|verify} messages.
     * @function encode
     * @memberof LoadGame
     * @static
     * @param {ILoadGame} message LoadGame message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    LoadGame.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.filename != null && Object.hasOwnProperty.call(message, "filename"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.filename);
        return writer;
    };

    /**
     * Encodes the specified LoadGame message, length delimited. Does not implicitly {@link LoadGame.verify|verify} messages.
     * @function encodeDelimited
     * @memberof LoadGame
     * @static
     * @param {ILoadGame} message LoadGame message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    LoadGame.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a LoadGame message from the specified reader or buffer.
     * @function decode
     * @memberof LoadGame
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {LoadGame} LoadGame
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    LoadGame.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.LoadGame();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.filename = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a LoadGame message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof LoadGame
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {LoadGame} LoadGame
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    LoadGame.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a LoadGame message.
     * @function verify
     * @memberof LoadGame
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    LoadGame.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.filename != null && message.hasOwnProperty("filename"))
            if (!$util.isString(message.filename))
                return "filename: string expected";
        return null;
    };

    /**
     * Creates a LoadGame message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof LoadGame
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {LoadGame} LoadGame
     */
    LoadGame.fromObject = function fromObject(object) {
        if (object instanceof $root.LoadGame)
            return object;
        var message = new $root.LoadGame();
        if (object.filename != null)
            message.filename = String(object.filename);
        return message;
    };

    /**
     * Creates a plain object from a LoadGame message. Also converts values to other types if specified.
     * @function toObject
     * @memberof LoadGame
     * @static
     * @param {LoadGame} message LoadGame
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    LoadGame.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.filename = "";
        if (message.filename != null && message.hasOwnProperty("filename"))
            object.filename = message.filename;
        return object;
    };

    /**
     * Converts this LoadGame to JSON.
     * @function toJSON
     * @memberof LoadGame
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    LoadGame.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return LoadGame;
})();

$root.RegisterGameConnection = (function() {

    /**
     * Properties of a RegisterGameConnection.
     * @exports IRegisterGameConnection
     * @interface IRegisterGameConnection
     * @property {string|null} [gameID] RegisterGameConnection gameID
     */

    /**
     * Constructs a new RegisterGameConnection.
     * @exports RegisterGameConnection
     * @classdesc Represents a RegisterGameConnection.
     * @implements IRegisterGameConnection
     * @constructor
     * @param {IRegisterGameConnection=} [properties] Properties to set
     */
    function RegisterGameConnection(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * RegisterGameConnection gameID.
     * @member {string} gameID
     * @memberof RegisterGameConnection
     * @instance
     */
    RegisterGameConnection.prototype.gameID = "";

    /**
     * Creates a new RegisterGameConnection instance using the specified properties.
     * @function create
     * @memberof RegisterGameConnection
     * @static
     * @param {IRegisterGameConnection=} [properties] Properties to set
     * @returns {RegisterGameConnection} RegisterGameConnection instance
     */
    RegisterGameConnection.create = function create(properties) {
        return new RegisterGameConnection(properties);
    };

    /**
     * Encodes the specified RegisterGameConnection message. Does not implicitly {@link RegisterGameConnection.verify|verify} messages.
     * @function encode
     * @memberof RegisterGameConnection
     * @static
     * @param {IRegisterGameConnection} message RegisterGameConnection message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    RegisterGameConnection.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.gameID != null && Object.hasOwnProperty.call(message, "gameID"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.gameID);
        return writer;
    };

    /**
     * Encodes the specified RegisterGameConnection message, length delimited. Does not implicitly {@link RegisterGameConnection.verify|verify} messages.
     * @function encodeDelimited
     * @memberof RegisterGameConnection
     * @static
     * @param {IRegisterGameConnection} message RegisterGameConnection message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    RegisterGameConnection.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a RegisterGameConnection message from the specified reader or buffer.
     * @function decode
     * @memberof RegisterGameConnection
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {RegisterGameConnection} RegisterGameConnection
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    RegisterGameConnection.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.RegisterGameConnection();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.gameID = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a RegisterGameConnection message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof RegisterGameConnection
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {RegisterGameConnection} RegisterGameConnection
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    RegisterGameConnection.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a RegisterGameConnection message.
     * @function verify
     * @memberof RegisterGameConnection
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    RegisterGameConnection.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.gameID != null && message.hasOwnProperty("gameID"))
            if (!$util.isString(message.gameID))
                return "gameID: string expected";
        return null;
    };

    /**
     * Creates a RegisterGameConnection message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof RegisterGameConnection
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {RegisterGameConnection} RegisterGameConnection
     */
    RegisterGameConnection.fromObject = function fromObject(object) {
        if (object instanceof $root.RegisterGameConnection)
            return object;
        var message = new $root.RegisterGameConnection();
        if (object.gameID != null)
            message.gameID = String(object.gameID);
        return message;
    };

    /**
     * Creates a plain object from a RegisterGameConnection message. Also converts values to other types if specified.
     * @function toObject
     * @memberof RegisterGameConnection
     * @static
     * @param {RegisterGameConnection} message RegisterGameConnection
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    RegisterGameConnection.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.gameID = "";
        if (message.gameID != null && message.hasOwnProperty("gameID"))
            object.gameID = message.gameID;
        return object;
    };

    /**
     * Converts this RegisterGameConnection to JSON.
     * @function toJSON
     * @memberof RegisterGameConnection
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    RegisterGameConnection.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return RegisterGameConnection;
})();

$root.ClientMessage = (function() {

    /**
     * Properties of a ClientMessage.
     * @exports IClientMessage
     * @interface IClientMessage
     * @property {IJoinQueue|null} [joinQueue] ClientMessage joinQueue
     * @property {IRegisterGameConnection|null} [registerGameConnection] ClientMessage registerGameConnection
     * @property {ILoadGame|null} [loadGame] ClientMessage loadGame
     */

    /**
     * Constructs a new ClientMessage.
     * @exports ClientMessage
     * @classdesc Represents a ClientMessage.
     * @implements IClientMessage
     * @constructor
     * @param {IClientMessage=} [properties] Properties to set
     */
    function ClientMessage(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * ClientMessage joinQueue.
     * @member {IJoinQueue|null|undefined} joinQueue
     * @memberof ClientMessage
     * @instance
     */
    ClientMessage.prototype.joinQueue = null;

    /**
     * ClientMessage registerGameConnection.
     * @member {IRegisterGameConnection|null|undefined} registerGameConnection
     * @memberof ClientMessage
     * @instance
     */
    ClientMessage.prototype.registerGameConnection = null;

    /**
     * ClientMessage loadGame.
     * @member {ILoadGame|null|undefined} loadGame
     * @memberof ClientMessage
     * @instance
     */
    ClientMessage.prototype.loadGame = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * ClientMessage payload.
     * @member {"joinQueue"|"registerGameConnection"|"loadGame"|undefined} payload
     * @memberof ClientMessage
     * @instance
     */
    Object.defineProperty(ClientMessage.prototype, "payload", {
        get: $util.oneOfGetter($oneOfFields = ["joinQueue", "registerGameConnection", "loadGame"]),
        set: $util.oneOfSetter($oneOfFields)
    });

    /**
     * Creates a new ClientMessage instance using the specified properties.
     * @function create
     * @memberof ClientMessage
     * @static
     * @param {IClientMessage=} [properties] Properties to set
     * @returns {ClientMessage} ClientMessage instance
     */
    ClientMessage.create = function create(properties) {
        return new ClientMessage(properties);
    };

    /**
     * Encodes the specified ClientMessage message. Does not implicitly {@link ClientMessage.verify|verify} messages.
     * @function encode
     * @memberof ClientMessage
     * @static
     * @param {IClientMessage} message ClientMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ClientMessage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.joinQueue != null && Object.hasOwnProperty.call(message, "joinQueue"))
            $root.JoinQueue.encode(message.joinQueue, writer.uint32(/* id 6, wireType 2 =*/50).fork()).ldelim();
        if (message.registerGameConnection != null && Object.hasOwnProperty.call(message, "registerGameConnection"))
            $root.RegisterGameConnection.encode(message.registerGameConnection, writer.uint32(/* id 11, wireType 2 =*/90).fork()).ldelim();
        if (message.loadGame != null && Object.hasOwnProperty.call(message, "loadGame"))
            $root.LoadGame.encode(message.loadGame, writer.uint32(/* id 12, wireType 2 =*/98).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified ClientMessage message, length delimited. Does not implicitly {@link ClientMessage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof ClientMessage
     * @static
     * @param {IClientMessage} message ClientMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ClientMessage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a ClientMessage message from the specified reader or buffer.
     * @function decode
     * @memberof ClientMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {ClientMessage} ClientMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ClientMessage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.ClientMessage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 6:
                message.joinQueue = $root.JoinQueue.decode(reader, reader.uint32());
                break;
            case 11:
                message.registerGameConnection = $root.RegisterGameConnection.decode(reader, reader.uint32());
                break;
            case 12:
                message.loadGame = $root.LoadGame.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a ClientMessage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof ClientMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {ClientMessage} ClientMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ClientMessage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a ClientMessage message.
     * @function verify
     * @memberof ClientMessage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    ClientMessage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        var properties = {};
        if (message.joinQueue != null && message.hasOwnProperty("joinQueue")) {
            properties.payload = 1;
            {
                var error = $root.JoinQueue.verify(message.joinQueue);
                if (error)
                    return "joinQueue." + error;
            }
        }
        if (message.registerGameConnection != null && message.hasOwnProperty("registerGameConnection")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.RegisterGameConnection.verify(message.registerGameConnection);
                if (error)
                    return "registerGameConnection." + error;
            }
        }
        if (message.loadGame != null && message.hasOwnProperty("loadGame")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.LoadGame.verify(message.loadGame);
                if (error)
                    return "loadGame." + error;
            }
        }
        return null;
    };

    /**
     * Creates a ClientMessage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof ClientMessage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {ClientMessage} ClientMessage
     */
    ClientMessage.fromObject = function fromObject(object) {
        if (object instanceof $root.ClientMessage)
            return object;
        var message = new $root.ClientMessage();
        if (object.joinQueue != null) {
            if (typeof object.joinQueue !== "object")
                throw TypeError(".ClientMessage.joinQueue: object expected");
            message.joinQueue = $root.JoinQueue.fromObject(object.joinQueue);
        }
        if (object.registerGameConnection != null) {
            if (typeof object.registerGameConnection !== "object")
                throw TypeError(".ClientMessage.registerGameConnection: object expected");
            message.registerGameConnection = $root.RegisterGameConnection.fromObject(object.registerGameConnection);
        }
        if (object.loadGame != null) {
            if (typeof object.loadGame !== "object")
                throw TypeError(".ClientMessage.loadGame: object expected");
            message.loadGame = $root.LoadGame.fromObject(object.loadGame);
        }
        return message;
    };

    /**
     * Creates a plain object from a ClientMessage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof ClientMessage
     * @static
     * @param {ClientMessage} message ClientMessage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    ClientMessage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (message.joinQueue != null && message.hasOwnProperty("joinQueue")) {
            object.joinQueue = $root.JoinQueue.toObject(message.joinQueue, options);
            if (options.oneofs)
                object.payload = "joinQueue";
        }
        if (message.registerGameConnection != null && message.hasOwnProperty("registerGameConnection")) {
            object.registerGameConnection = $root.RegisterGameConnection.toObject(message.registerGameConnection, options);
            if (options.oneofs)
                object.payload = "registerGameConnection";
        }
        if (message.loadGame != null && message.hasOwnProperty("loadGame")) {
            object.loadGame = $root.LoadGame.toObject(message.loadGame, options);
            if (options.oneofs)
                object.payload = "loadGame";
        }
        return object;
    };

    /**
     * Converts this ClientMessage to JSON.
     * @function toJSON
     * @memberof ClientMessage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    ClientMessage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return ClientMessage;
})();

$root.UpdateGameState = (function() {

    /**
     * Properties of an UpdateGameState.
     * @exports IUpdateGameState
     * @interface IUpdateGameState
     * @property {IGameState|null} [game] UpdateGameState game
     */

    /**
     * Constructs a new UpdateGameState.
     * @exports UpdateGameState
     * @classdesc Represents an UpdateGameState.
     * @implements IUpdateGameState
     * @constructor
     * @param {IUpdateGameState=} [properties] Properties to set
     */
    function UpdateGameState(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * UpdateGameState game.
     * @member {IGameState|null|undefined} game
     * @memberof UpdateGameState
     * @instance
     */
    UpdateGameState.prototype.game = null;

    /**
     * Creates a new UpdateGameState instance using the specified properties.
     * @function create
     * @memberof UpdateGameState
     * @static
     * @param {IUpdateGameState=} [properties] Properties to set
     * @returns {UpdateGameState} UpdateGameState instance
     */
    UpdateGameState.create = function create(properties) {
        return new UpdateGameState(properties);
    };

    /**
     * Encodes the specified UpdateGameState message. Does not implicitly {@link UpdateGameState.verify|verify} messages.
     * @function encode
     * @memberof UpdateGameState
     * @static
     * @param {IUpdateGameState} message UpdateGameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    UpdateGameState.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified UpdateGameState message, length delimited. Does not implicitly {@link UpdateGameState.verify|verify} messages.
     * @function encodeDelimited
     * @memberof UpdateGameState
     * @static
     * @param {IUpdateGameState} message UpdateGameState message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    UpdateGameState.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an UpdateGameState message from the specified reader or buffer.
     * @function decode
     * @memberof UpdateGameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {UpdateGameState} UpdateGameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    UpdateGameState.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.UpdateGameState();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an UpdateGameState message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof UpdateGameState
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {UpdateGameState} UpdateGameState
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    UpdateGameState.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an UpdateGameState message.
     * @function verify
     * @memberof UpdateGameState
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    UpdateGameState.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        return null;
    };

    /**
     * Creates an UpdateGameState message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof UpdateGameState
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {UpdateGameState} UpdateGameState
     */
    UpdateGameState.fromObject = function fromObject(object) {
        if (object instanceof $root.UpdateGameState)
            return object;
        var message = new $root.UpdateGameState();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".UpdateGameState.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        return message;
    };

    /**
     * Creates a plain object from an UpdateGameState message. Also converts values to other types if specified.
     * @function toObject
     * @memberof UpdateGameState
     * @static
     * @param {UpdateGameState} message UpdateGameState
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    UpdateGameState.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.game = null;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        return object;
    };

    /**
     * Converts this UpdateGameState to JSON.
     * @function toJSON
     * @memberof UpdateGameState
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    UpdateGameState.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return UpdateGameState;
})();

$root.GameEnd = (function() {

    /**
     * Properties of a GameEnd.
     * @exports IGameEnd
     * @interface IGameEnd
     * @property {IGameState|null} [game] GameEnd game
     * @property {boolean|null} [win] GameEnd win
     */

    /**
     * Constructs a new GameEnd.
     * @exports GameEnd
     * @classdesc Represents a GameEnd.
     * @implements IGameEnd
     * @constructor
     * @param {IGameEnd=} [properties] Properties to set
     */
    function GameEnd(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * GameEnd game.
     * @member {IGameState|null|undefined} game
     * @memberof GameEnd
     * @instance
     */
    GameEnd.prototype.game = null;

    /**
     * GameEnd win.
     * @member {boolean} win
     * @memberof GameEnd
     * @instance
     */
    GameEnd.prototype.win = false;

    /**
     * Creates a new GameEnd instance using the specified properties.
     * @function create
     * @memberof GameEnd
     * @static
     * @param {IGameEnd=} [properties] Properties to set
     * @returns {GameEnd} GameEnd instance
     */
    GameEnd.create = function create(properties) {
        return new GameEnd(properties);
    };

    /**
     * Encodes the specified GameEnd message. Does not implicitly {@link GameEnd.verify|verify} messages.
     * @function encode
     * @memberof GameEnd
     * @static
     * @param {IGameEnd} message GameEnd message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    GameEnd.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.win != null && Object.hasOwnProperty.call(message, "win"))
            writer.uint32(/* id 2, wireType 0 =*/16).bool(message.win);
        return writer;
    };

    /**
     * Encodes the specified GameEnd message, length delimited. Does not implicitly {@link GameEnd.verify|verify} messages.
     * @function encodeDelimited
     * @memberof GameEnd
     * @static
     * @param {IGameEnd} message GameEnd message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    GameEnd.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a GameEnd message from the specified reader or buffer.
     * @function decode
     * @memberof GameEnd
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {GameEnd} GameEnd
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    GameEnd.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.GameEnd();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                message.win = reader.bool();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a GameEnd message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof GameEnd
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {GameEnd} GameEnd
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    GameEnd.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a GameEnd message.
     * @function verify
     * @memberof GameEnd
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    GameEnd.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.win != null && message.hasOwnProperty("win"))
            if (typeof message.win !== "boolean")
                return "win: boolean expected";
        return null;
    };

    /**
     * Creates a GameEnd message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof GameEnd
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {GameEnd} GameEnd
     */
    GameEnd.fromObject = function fromObject(object) {
        if (object instanceof $root.GameEnd)
            return object;
        var message = new $root.GameEnd();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".GameEnd.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.win != null)
            message.win = Boolean(object.win);
        return message;
    };

    /**
     * Creates a plain object from a GameEnd message. Also converts values to other types if specified.
     * @function toObject
     * @memberof GameEnd
     * @static
     * @param {GameEnd} message GameEnd
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    GameEnd.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.game = null;
            object.win = false;
        }
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.win != null && message.hasOwnProperty("win"))
            object.win = message.win;
        return object;
    };

    /**
     * Converts this GameEnd to JSON.
     * @function toJSON
     * @memberof GameEnd
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    GameEnd.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return GameEnd;
})();

$root.OrderCards = (function() {

    /**
     * Properties of an OrderCards.
     * @exports IOrderCards
     * @interface IOrderCards
     * @property {IGameState|null} [game] OrderCards game
     * @property {Array.<ICard>|null} [cardsToOrder] OrderCards cardsToOrder
     * @property {string|null} [message] OrderCards message
     */

    /**
     * Constructs a new OrderCards.
     * @exports OrderCards
     * @classdesc Represents an OrderCards.
     * @implements IOrderCards
     * @constructor
     * @param {IOrderCards=} [properties] Properties to set
     */
    function OrderCards(properties) {
        this.cardsToOrder = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * OrderCards game.
     * @member {IGameState|null|undefined} game
     * @memberof OrderCards
     * @instance
     */
    OrderCards.prototype.game = null;

    /**
     * OrderCards cardsToOrder.
     * @member {Array.<ICard>} cardsToOrder
     * @memberof OrderCards
     * @instance
     */
    OrderCards.prototype.cardsToOrder = $util.emptyArray;

    /**
     * OrderCards message.
     * @member {string} message
     * @memberof OrderCards
     * @instance
     */
    OrderCards.prototype.message = "";

    /**
     * Creates a new OrderCards instance using the specified properties.
     * @function create
     * @memberof OrderCards
     * @static
     * @param {IOrderCards=} [properties] Properties to set
     * @returns {OrderCards} OrderCards instance
     */
    OrderCards.create = function create(properties) {
        return new OrderCards(properties);
    };

    /**
     * Encodes the specified OrderCards message. Does not implicitly {@link OrderCards.verify|verify} messages.
     * @function encode
     * @memberof OrderCards
     * @static
     * @param {IOrderCards} message OrderCards message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    OrderCards.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.cardsToOrder != null && message.cardsToOrder.length)
            for (var i = 0; i < message.cardsToOrder.length; ++i)
                $root.Card.encode(message.cardsToOrder[i], writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        if (message.message != null && Object.hasOwnProperty.call(message, "message"))
            writer.uint32(/* id 7, wireType 2 =*/58).string(message.message);
        return writer;
    };

    /**
     * Encodes the specified OrderCards message, length delimited. Does not implicitly {@link OrderCards.verify|verify} messages.
     * @function encodeDelimited
     * @memberof OrderCards
     * @static
     * @param {IOrderCards} message OrderCards message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    OrderCards.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an OrderCards message from the specified reader or buffer.
     * @function decode
     * @memberof OrderCards
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {OrderCards} OrderCards
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    OrderCards.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.OrderCards();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                if (!(message.cardsToOrder && message.cardsToOrder.length))
                    message.cardsToOrder = [];
                message.cardsToOrder.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 7:
                message.message = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an OrderCards message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof OrderCards
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {OrderCards} OrderCards
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    OrderCards.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an OrderCards message.
     * @function verify
     * @memberof OrderCards
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    OrderCards.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.cardsToOrder != null && message.hasOwnProperty("cardsToOrder")) {
            if (!Array.isArray(message.cardsToOrder))
                return "cardsToOrder: array expected";
            for (var i = 0; i < message.cardsToOrder.length; ++i) {
                var error = $root.Card.verify(message.cardsToOrder[i]);
                if (error)
                    return "cardsToOrder." + error;
            }
        }
        if (message.message != null && message.hasOwnProperty("message"))
            if (!$util.isString(message.message))
                return "message: string expected";
        return null;
    };

    /**
     * Creates an OrderCards message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof OrderCards
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {OrderCards} OrderCards
     */
    OrderCards.fromObject = function fromObject(object) {
        if (object instanceof $root.OrderCards)
            return object;
        var message = new $root.OrderCards();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".OrderCards.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.cardsToOrder) {
            if (!Array.isArray(object.cardsToOrder))
                throw TypeError(".OrderCards.cardsToOrder: array expected");
            message.cardsToOrder = [];
            for (var i = 0; i < object.cardsToOrder.length; ++i) {
                if (typeof object.cardsToOrder[i] !== "object")
                    throw TypeError(".OrderCards.cardsToOrder: object expected");
                message.cardsToOrder[i] = $root.Card.fromObject(object.cardsToOrder[i]);
            }
        }
        if (object.message != null)
            message.message = String(object.message);
        return message;
    };

    /**
     * Creates a plain object from an OrderCards message. Also converts values to other types if specified.
     * @function toObject
     * @memberof OrderCards
     * @static
     * @param {OrderCards} message OrderCards
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    OrderCards.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.cardsToOrder = [];
        if (options.defaults) {
            object.game = null;
            object.message = "";
        }
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.cardsToOrder && message.cardsToOrder.length) {
            object.cardsToOrder = [];
            for (var j = 0; j < message.cardsToOrder.length; ++j)
                object.cardsToOrder[j] = $root.Card.toObject(message.cardsToOrder[j], options);
        }
        if (message.message != null && message.hasOwnProperty("message"))
            object.message = message.message;
        return object;
    };

    /**
     * Converts this OrderCards to JSON.
     * @function toJSON
     * @memberof OrderCards
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    OrderCards.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return OrderCards;
})();

$root.SelectFrom = (function() {

    /**
     * Properties of a SelectFrom.
     * @exports ISelectFrom
     * @interface ISelectFrom
     * @property {SelectFromType|null} [messageType] SelectFrom messageType
     * @property {IGameState|null} [game] SelectFrom game
     * @property {number|null} [selectionCount] SelectFrom selectionCount
     * @property {Array.<ICard>|null} [candidates] SelectFrom candidates
     * @property {Array.<string>|null} [selectable] SelectFrom selectable
     * @property {boolean|null} [upTo] SelectFrom upTo
     * @property {string|null} [message] SelectFrom message
     */

    /**
     * Constructs a new SelectFrom.
     * @exports SelectFrom
     * @classdesc Represents a SelectFrom.
     * @implements ISelectFrom
     * @constructor
     * @param {ISelectFrom=} [properties] Properties to set
     */
    function SelectFrom(properties) {
        this.candidates = [];
        this.selectable = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectFrom messageType.
     * @member {SelectFromType} messageType
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.messageType = 0;

    /**
     * SelectFrom game.
     * @member {IGameState|null|undefined} game
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.game = null;

    /**
     * SelectFrom selectionCount.
     * @member {number} selectionCount
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.selectionCount = 0;

    /**
     * SelectFrom candidates.
     * @member {Array.<ICard>} candidates
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.candidates = $util.emptyArray;

    /**
     * SelectFrom selectable.
     * @member {Array.<string>} selectable
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.selectable = $util.emptyArray;

    /**
     * SelectFrom upTo.
     * @member {boolean} upTo
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.upTo = false;

    /**
     * SelectFrom message.
     * @member {string} message
     * @memberof SelectFrom
     * @instance
     */
    SelectFrom.prototype.message = "";

    /**
     * Creates a new SelectFrom instance using the specified properties.
     * @function create
     * @memberof SelectFrom
     * @static
     * @param {ISelectFrom=} [properties] Properties to set
     * @returns {SelectFrom} SelectFrom instance
     */
    SelectFrom.create = function create(properties) {
        return new SelectFrom(properties);
    };

    /**
     * Encodes the specified SelectFrom message. Does not implicitly {@link SelectFrom.verify|verify} messages.
     * @function encode
     * @memberof SelectFrom
     * @static
     * @param {ISelectFrom} message SelectFrom message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectFrom.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.messageType != null && Object.hasOwnProperty.call(message, "messageType"))
            writer.uint32(/* id 1, wireType 0 =*/8).int32(message.messageType);
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        if (message.selectionCount != null && Object.hasOwnProperty.call(message, "selectionCount"))
            writer.uint32(/* id 3, wireType 0 =*/24).int32(message.selectionCount);
        if (message.candidates != null && message.candidates.length)
            for (var i = 0; i < message.candidates.length; ++i)
                $root.Card.encode(message.candidates[i], writer.uint32(/* id 4, wireType 2 =*/34).fork()).ldelim();
        if (message.selectable != null && message.selectable.length)
            for (var i = 0; i < message.selectable.length; ++i)
                writer.uint32(/* id 5, wireType 2 =*/42).string(message.selectable[i]);
        if (message.upTo != null && Object.hasOwnProperty.call(message, "upTo"))
            writer.uint32(/* id 6, wireType 0 =*/48).bool(message.upTo);
        if (message.message != null && Object.hasOwnProperty.call(message, "message"))
            writer.uint32(/* id 7, wireType 2 =*/58).string(message.message);
        return writer;
    };

    /**
     * Encodes the specified SelectFrom message, length delimited. Does not implicitly {@link SelectFrom.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectFrom
     * @static
     * @param {ISelectFrom} message SelectFrom message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectFrom.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectFrom message from the specified reader or buffer.
     * @function decode
     * @memberof SelectFrom
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectFrom} SelectFrom
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectFrom.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectFrom();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.messageType = reader.int32();
                break;
            case 2:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 3:
                message.selectionCount = reader.int32();
                break;
            case 4:
                if (!(message.candidates && message.candidates.length))
                    message.candidates = [];
                message.candidates.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 5:
                if (!(message.selectable && message.selectable.length))
                    message.selectable = [];
                message.selectable.push(reader.string());
                break;
            case 6:
                message.upTo = reader.bool();
                break;
            case 7:
                message.message = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectFrom message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectFrom
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectFrom} SelectFrom
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectFrom.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectFrom message.
     * @function verify
     * @memberof SelectFrom
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectFrom.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.messageType != null && message.hasOwnProperty("messageType"))
            switch (message.messageType) {
            default:
                return "messageType: enum value expected";
            case 0:
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            }
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.selectionCount != null && message.hasOwnProperty("selectionCount"))
            if (!$util.isInteger(message.selectionCount))
                return "selectionCount: integer expected";
        if (message.candidates != null && message.hasOwnProperty("candidates")) {
            if (!Array.isArray(message.candidates))
                return "candidates: array expected";
            for (var i = 0; i < message.candidates.length; ++i) {
                var error = $root.Card.verify(message.candidates[i]);
                if (error)
                    return "candidates." + error;
            }
        }
        if (message.selectable != null && message.hasOwnProperty("selectable")) {
            if (!Array.isArray(message.selectable))
                return "selectable: array expected";
            for (var i = 0; i < message.selectable.length; ++i)
                if (!$util.isString(message.selectable[i]))
                    return "selectable: string[] expected";
        }
        if (message.upTo != null && message.hasOwnProperty("upTo"))
            if (typeof message.upTo !== "boolean")
                return "upTo: boolean expected";
        if (message.message != null && message.hasOwnProperty("message"))
            if (!$util.isString(message.message))
                return "message: string expected";
        return null;
    };

    /**
     * Creates a SelectFrom message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectFrom
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectFrom} SelectFrom
     */
    SelectFrom.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectFrom)
            return object;
        var message = new $root.SelectFrom();
        switch (object.messageType) {
        case "NOTYPE":
        case 0:
            message.messageType = 0;
            break;
        case "LIST":
        case 1:
            message.messageType = 1;
            break;
        case "HAND":
        case 3:
            message.messageType = 3;
            break;
        case "PLAY":
        case 4:
            message.messageType = 4;
            break;
        case "DISCARD_PILE":
        case 5:
            message.messageType = 5;
            break;
        case "VOID":
        case 6:
            message.messageType = 6;
            break;
        case "STACK":
        case 7:
            message.messageType = 7;
            break;
        }
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".SelectFrom.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.selectionCount != null)
            message.selectionCount = object.selectionCount | 0;
        if (object.candidates) {
            if (!Array.isArray(object.candidates))
                throw TypeError(".SelectFrom.candidates: array expected");
            message.candidates = [];
            for (var i = 0; i < object.candidates.length; ++i) {
                if (typeof object.candidates[i] !== "object")
                    throw TypeError(".SelectFrom.candidates: object expected");
                message.candidates[i] = $root.Card.fromObject(object.candidates[i]);
            }
        }
        if (object.selectable) {
            if (!Array.isArray(object.selectable))
                throw TypeError(".SelectFrom.selectable: array expected");
            message.selectable = [];
            for (var i = 0; i < object.selectable.length; ++i)
                message.selectable[i] = String(object.selectable[i]);
        }
        if (object.upTo != null)
            message.upTo = Boolean(object.upTo);
        if (object.message != null)
            message.message = String(object.message);
        return message;
    };

    /**
     * Creates a plain object from a SelectFrom message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectFrom
     * @static
     * @param {SelectFrom} message SelectFrom
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectFrom.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults) {
            object.candidates = [];
            object.selectable = [];
        }
        if (options.defaults) {
            object.messageType = options.enums === String ? "NOTYPE" : 0;
            object.game = null;
            object.selectionCount = 0;
            object.upTo = false;
            object.message = "";
        }
        if (message.messageType != null && message.hasOwnProperty("messageType"))
            object.messageType = options.enums === String ? $root.SelectFromType[message.messageType] : message.messageType;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.selectionCount != null && message.hasOwnProperty("selectionCount"))
            object.selectionCount = message.selectionCount;
        if (message.candidates && message.candidates.length) {
            object.candidates = [];
            for (var j = 0; j < message.candidates.length; ++j)
                object.candidates[j] = $root.Card.toObject(message.candidates[j], options);
        }
        if (message.selectable && message.selectable.length) {
            object.selectable = [];
            for (var j = 0; j < message.selectable.length; ++j)
                object.selectable[j] = message.selectable[j];
        }
        if (message.upTo != null && message.hasOwnProperty("upTo"))
            object.upTo = message.upTo;
        if (message.message != null && message.hasOwnProperty("message"))
            object.message = message.message;
        return object;
    };

    /**
     * Converts this SelectFrom to JSON.
     * @function toJSON
     * @memberof SelectFrom
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectFrom.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectFrom;
})();

$root.SelectXValue = (function() {

    /**
     * Properties of a SelectXValue.
     * @exports ISelectXValue
     * @interface ISelectXValue
     * @property {IGameState|null} [game] SelectXValue game
     * @property {number|null} [maxXValue] SelectXValue maxXValue
     * @property {string|null} [message] SelectXValue message
     */

    /**
     * Constructs a new SelectXValue.
     * @exports SelectXValue
     * @classdesc Represents a SelectXValue.
     * @implements ISelectXValue
     * @constructor
     * @param {ISelectXValue=} [properties] Properties to set
     */
    function SelectXValue(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectXValue game.
     * @member {IGameState|null|undefined} game
     * @memberof SelectXValue
     * @instance
     */
    SelectXValue.prototype.game = null;

    /**
     * SelectXValue maxXValue.
     * @member {number} maxXValue
     * @memberof SelectXValue
     * @instance
     */
    SelectXValue.prototype.maxXValue = 0;

    /**
     * SelectXValue message.
     * @member {string} message
     * @memberof SelectXValue
     * @instance
     */
    SelectXValue.prototype.message = "";

    /**
     * Creates a new SelectXValue instance using the specified properties.
     * @function create
     * @memberof SelectXValue
     * @static
     * @param {ISelectXValue=} [properties] Properties to set
     * @returns {SelectXValue} SelectXValue instance
     */
    SelectXValue.create = function create(properties) {
        return new SelectXValue(properties);
    };

    /**
     * Encodes the specified SelectXValue message. Does not implicitly {@link SelectXValue.verify|verify} messages.
     * @function encode
     * @memberof SelectXValue
     * @static
     * @param {ISelectXValue} message SelectXValue message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectXValue.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.maxXValue != null && Object.hasOwnProperty.call(message, "maxXValue"))
            writer.uint32(/* id 2, wireType 0 =*/16).int32(message.maxXValue);
        if (message.message != null && Object.hasOwnProperty.call(message, "message"))
            writer.uint32(/* id 7, wireType 2 =*/58).string(message.message);
        return writer;
    };

    /**
     * Encodes the specified SelectXValue message, length delimited. Does not implicitly {@link SelectXValue.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectXValue
     * @static
     * @param {ISelectXValue} message SelectXValue message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectXValue.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectXValue message from the specified reader or buffer.
     * @function decode
     * @memberof SelectXValue
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectXValue} SelectXValue
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectXValue.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectXValue();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                message.maxXValue = reader.int32();
                break;
            case 7:
                message.message = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectXValue message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectXValue
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectXValue} SelectXValue
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectXValue.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectXValue message.
     * @function verify
     * @memberof SelectXValue
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectXValue.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.maxXValue != null && message.hasOwnProperty("maxXValue"))
            if (!$util.isInteger(message.maxXValue))
                return "maxXValue: integer expected";
        if (message.message != null && message.hasOwnProperty("message"))
            if (!$util.isString(message.message))
                return "message: string expected";
        return null;
    };

    /**
     * Creates a SelectXValue message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectXValue
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectXValue} SelectXValue
     */
    SelectXValue.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectXValue)
            return object;
        var message = new $root.SelectXValue();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".SelectXValue.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.maxXValue != null)
            message.maxXValue = object.maxXValue | 0;
        if (object.message != null)
            message.message = String(object.message);
        return message;
    };

    /**
     * Creates a plain object from a SelectXValue message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectXValue
     * @static
     * @param {SelectXValue} message SelectXValue
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectXValue.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.game = null;
            object.maxXValue = 0;
            object.message = "";
        }
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.maxXValue != null && message.hasOwnProperty("maxXValue"))
            object.maxXValue = message.maxXValue;
        if (message.message != null && message.hasOwnProperty("message"))
            object.message = message.message;
        return object;
    };

    /**
     * Converts this SelectXValue to JSON.
     * @function toJSON
     * @memberof SelectXValue
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectXValue.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectXValue;
})();

$root.SelectAttackers = (function() {

    /**
     * Properties of a SelectAttackers.
     * @exports ISelectAttackers
     * @interface ISelectAttackers
     * @property {IGameState|null} [game] SelectAttackers game
     * @property {Array.<IAttacker>|null} [possibleAttackers] SelectAttackers possibleAttackers
     */

    /**
     * Constructs a new SelectAttackers.
     * @exports SelectAttackers
     * @classdesc Represents a SelectAttackers.
     * @implements ISelectAttackers
     * @constructor
     * @param {ISelectAttackers=} [properties] Properties to set
     */
    function SelectAttackers(properties) {
        this.possibleAttackers = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectAttackers game.
     * @member {IGameState|null|undefined} game
     * @memberof SelectAttackers
     * @instance
     */
    SelectAttackers.prototype.game = null;

    /**
     * SelectAttackers possibleAttackers.
     * @member {Array.<IAttacker>} possibleAttackers
     * @memberof SelectAttackers
     * @instance
     */
    SelectAttackers.prototype.possibleAttackers = $util.emptyArray;

    /**
     * Creates a new SelectAttackers instance using the specified properties.
     * @function create
     * @memberof SelectAttackers
     * @static
     * @param {ISelectAttackers=} [properties] Properties to set
     * @returns {SelectAttackers} SelectAttackers instance
     */
    SelectAttackers.create = function create(properties) {
        return new SelectAttackers(properties);
    };

    /**
     * Encodes the specified SelectAttackers message. Does not implicitly {@link SelectAttackers.verify|verify} messages.
     * @function encode
     * @memberof SelectAttackers
     * @static
     * @param {ISelectAttackers} message SelectAttackers message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectAttackers.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.possibleAttackers != null && message.possibleAttackers.length)
            for (var i = 0; i < message.possibleAttackers.length; ++i)
                $root.Attacker.encode(message.possibleAttackers[i], writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified SelectAttackers message, length delimited. Does not implicitly {@link SelectAttackers.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectAttackers
     * @static
     * @param {ISelectAttackers} message SelectAttackers message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectAttackers.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectAttackers message from the specified reader or buffer.
     * @function decode
     * @memberof SelectAttackers
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectAttackers} SelectAttackers
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectAttackers.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectAttackers();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                if (!(message.possibleAttackers && message.possibleAttackers.length))
                    message.possibleAttackers = [];
                message.possibleAttackers.push($root.Attacker.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectAttackers message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectAttackers
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectAttackers} SelectAttackers
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectAttackers.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectAttackers message.
     * @function verify
     * @memberof SelectAttackers
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectAttackers.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.possibleAttackers != null && message.hasOwnProperty("possibleAttackers")) {
            if (!Array.isArray(message.possibleAttackers))
                return "possibleAttackers: array expected";
            for (var i = 0; i < message.possibleAttackers.length; ++i) {
                var error = $root.Attacker.verify(message.possibleAttackers[i]);
                if (error)
                    return "possibleAttackers." + error;
            }
        }
        return null;
    };

    /**
     * Creates a SelectAttackers message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectAttackers
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectAttackers} SelectAttackers
     */
    SelectAttackers.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectAttackers)
            return object;
        var message = new $root.SelectAttackers();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".SelectAttackers.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.possibleAttackers) {
            if (!Array.isArray(object.possibleAttackers))
                throw TypeError(".SelectAttackers.possibleAttackers: array expected");
            message.possibleAttackers = [];
            for (var i = 0; i < object.possibleAttackers.length; ++i) {
                if (typeof object.possibleAttackers[i] !== "object")
                    throw TypeError(".SelectAttackers.possibleAttackers: object expected");
                message.possibleAttackers[i] = $root.Attacker.fromObject(object.possibleAttackers[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectAttackers message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectAttackers
     * @static
     * @param {SelectAttackers} message SelectAttackers
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectAttackers.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.possibleAttackers = [];
        if (options.defaults)
            object.game = null;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.possibleAttackers && message.possibleAttackers.length) {
            object.possibleAttackers = [];
            for (var j = 0; j < message.possibleAttackers.length; ++j)
                object.possibleAttackers[j] = $root.Attacker.toObject(message.possibleAttackers[j], options);
        }
        return object;
    };

    /**
     * Converts this SelectAttackers to JSON.
     * @function toJSON
     * @memberof SelectAttackers
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectAttackers.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectAttackers;
})();

$root.SelectBlockers = (function() {

    /**
     * Properties of a SelectBlockers.
     * @exports ISelectBlockers
     * @interface ISelectBlockers
     * @property {IGameState|null} [game] SelectBlockers game
     * @property {Array.<IBlocker>|null} [possibleBlockers] SelectBlockers possibleBlockers
     */

    /**
     * Constructs a new SelectBlockers.
     * @exports SelectBlockers
     * @classdesc Represents a SelectBlockers.
     * @implements ISelectBlockers
     * @constructor
     * @param {ISelectBlockers=} [properties] Properties to set
     */
    function SelectBlockers(properties) {
        this.possibleBlockers = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectBlockers game.
     * @member {IGameState|null|undefined} game
     * @memberof SelectBlockers
     * @instance
     */
    SelectBlockers.prototype.game = null;

    /**
     * SelectBlockers possibleBlockers.
     * @member {Array.<IBlocker>} possibleBlockers
     * @memberof SelectBlockers
     * @instance
     */
    SelectBlockers.prototype.possibleBlockers = $util.emptyArray;

    /**
     * Creates a new SelectBlockers instance using the specified properties.
     * @function create
     * @memberof SelectBlockers
     * @static
     * @param {ISelectBlockers=} [properties] Properties to set
     * @returns {SelectBlockers} SelectBlockers instance
     */
    SelectBlockers.create = function create(properties) {
        return new SelectBlockers(properties);
    };

    /**
     * Encodes the specified SelectBlockers message. Does not implicitly {@link SelectBlockers.verify|verify} messages.
     * @function encode
     * @memberof SelectBlockers
     * @static
     * @param {ISelectBlockers} message SelectBlockers message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectBlockers.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.possibleBlockers != null && message.possibleBlockers.length)
            for (var i = 0; i < message.possibleBlockers.length; ++i)
                $root.Blocker.encode(message.possibleBlockers[i], writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified SelectBlockers message, length delimited. Does not implicitly {@link SelectBlockers.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectBlockers
     * @static
     * @param {ISelectBlockers} message SelectBlockers message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectBlockers.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectBlockers message from the specified reader or buffer.
     * @function decode
     * @memberof SelectBlockers
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectBlockers} SelectBlockers
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectBlockers.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectBlockers();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                if (!(message.possibleBlockers && message.possibleBlockers.length))
                    message.possibleBlockers = [];
                message.possibleBlockers.push($root.Blocker.decode(reader, reader.uint32()));
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectBlockers message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectBlockers
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectBlockers} SelectBlockers
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectBlockers.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectBlockers message.
     * @function verify
     * @memberof SelectBlockers
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectBlockers.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.possibleBlockers != null && message.hasOwnProperty("possibleBlockers")) {
            if (!Array.isArray(message.possibleBlockers))
                return "possibleBlockers: array expected";
            for (var i = 0; i < message.possibleBlockers.length; ++i) {
                var error = $root.Blocker.verify(message.possibleBlockers[i]);
                if (error)
                    return "possibleBlockers." + error;
            }
        }
        return null;
    };

    /**
     * Creates a SelectBlockers message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectBlockers
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectBlockers} SelectBlockers
     */
    SelectBlockers.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectBlockers)
            return object;
        var message = new $root.SelectBlockers();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".SelectBlockers.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.possibleBlockers) {
            if (!Array.isArray(object.possibleBlockers))
                throw TypeError(".SelectBlockers.possibleBlockers: array expected");
            message.possibleBlockers = [];
            for (var i = 0; i < object.possibleBlockers.length; ++i) {
                if (typeof object.possibleBlockers[i] !== "object")
                    throw TypeError(".SelectBlockers.possibleBlockers: object expected");
                message.possibleBlockers[i] = $root.Blocker.fromObject(object.possibleBlockers[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectBlockers message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectBlockers
     * @static
     * @param {SelectBlockers} message SelectBlockers
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectBlockers.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.possibleBlockers = [];
        if (options.defaults)
            object.game = null;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.possibleBlockers && message.possibleBlockers.length) {
            object.possibleBlockers = [];
            for (var j = 0; j < message.possibleBlockers.length; ++j)
                object.possibleBlockers[j] = $root.Blocker.toObject(message.possibleBlockers[j], options);
        }
        return object;
    };

    /**
     * Converts this SelectBlockers to JSON.
     * @function toJSON
     * @memberof SelectBlockers
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectBlockers.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectBlockers;
})();

$root.AssignDamage = (function() {

    /**
     * Properties of an AssignDamage.
     * @exports IAssignDamage
     * @interface IAssignDamage
     * @property {IGameState|null} [game] AssignDamage game
     * @property {string|null} [damageSource] AssignDamage damageSource
     * @property {Array.<string>|null} [possibleTargets] AssignDamage possibleTargets
     * @property {number|null} [totalDamage] AssignDamage totalDamage
     * @property {boolean|null} [trample] AssignDamage trample
     */

    /**
     * Constructs a new AssignDamage.
     * @exports AssignDamage
     * @classdesc Represents an AssignDamage.
     * @implements IAssignDamage
     * @constructor
     * @param {IAssignDamage=} [properties] Properties to set
     */
    function AssignDamage(properties) {
        this.possibleTargets = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * AssignDamage game.
     * @member {IGameState|null|undefined} game
     * @memberof AssignDamage
     * @instance
     */
    AssignDamage.prototype.game = null;

    /**
     * AssignDamage damageSource.
     * @member {string} damageSource
     * @memberof AssignDamage
     * @instance
     */
    AssignDamage.prototype.damageSource = "";

    /**
     * AssignDamage possibleTargets.
     * @member {Array.<string>} possibleTargets
     * @memberof AssignDamage
     * @instance
     */
    AssignDamage.prototype.possibleTargets = $util.emptyArray;

    /**
     * AssignDamage totalDamage.
     * @member {number} totalDamage
     * @memberof AssignDamage
     * @instance
     */
    AssignDamage.prototype.totalDamage = 0;

    /**
     * AssignDamage trample.
     * @member {boolean} trample
     * @memberof AssignDamage
     * @instance
     */
    AssignDamage.prototype.trample = false;

    /**
     * Creates a new AssignDamage instance using the specified properties.
     * @function create
     * @memberof AssignDamage
     * @static
     * @param {IAssignDamage=} [properties] Properties to set
     * @returns {AssignDamage} AssignDamage instance
     */
    AssignDamage.create = function create(properties) {
        return new AssignDamage(properties);
    };

    /**
     * Encodes the specified AssignDamage message. Does not implicitly {@link AssignDamage.verify|verify} messages.
     * @function encode
     * @memberof AssignDamage
     * @static
     * @param {IAssignDamage} message AssignDamage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AssignDamage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.damageSource != null && Object.hasOwnProperty.call(message, "damageSource"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.damageSource);
        if (message.possibleTargets != null && message.possibleTargets.length)
            for (var i = 0; i < message.possibleTargets.length; ++i)
                writer.uint32(/* id 3, wireType 2 =*/26).string(message.possibleTargets[i]);
        if (message.totalDamage != null && Object.hasOwnProperty.call(message, "totalDamage"))
            writer.uint32(/* id 4, wireType 0 =*/32).int32(message.totalDamage);
        if (message.trample != null && Object.hasOwnProperty.call(message, "trample"))
            writer.uint32(/* id 5, wireType 0 =*/40).bool(message.trample);
        return writer;
    };

    /**
     * Encodes the specified AssignDamage message, length delimited. Does not implicitly {@link AssignDamage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof AssignDamage
     * @static
     * @param {IAssignDamage} message AssignDamage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    AssignDamage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes an AssignDamage message from the specified reader or buffer.
     * @function decode
     * @memberof AssignDamage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {AssignDamage} AssignDamage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AssignDamage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.AssignDamage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                message.damageSource = reader.string();
                break;
            case 3:
                if (!(message.possibleTargets && message.possibleTargets.length))
                    message.possibleTargets = [];
                message.possibleTargets.push(reader.string());
                break;
            case 4:
                message.totalDamage = reader.int32();
                break;
            case 5:
                message.trample = reader.bool();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes an AssignDamage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof AssignDamage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {AssignDamage} AssignDamage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    AssignDamage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies an AssignDamage message.
     * @function verify
     * @memberof AssignDamage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    AssignDamage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.damageSource != null && message.hasOwnProperty("damageSource"))
            if (!$util.isString(message.damageSource))
                return "damageSource: string expected";
        if (message.possibleTargets != null && message.hasOwnProperty("possibleTargets")) {
            if (!Array.isArray(message.possibleTargets))
                return "possibleTargets: array expected";
            for (var i = 0; i < message.possibleTargets.length; ++i)
                if (!$util.isString(message.possibleTargets[i]))
                    return "possibleTargets: string[] expected";
        }
        if (message.totalDamage != null && message.hasOwnProperty("totalDamage"))
            if (!$util.isInteger(message.totalDamage))
                return "totalDamage: integer expected";
        if (message.trample != null && message.hasOwnProperty("trample"))
            if (typeof message.trample !== "boolean")
                return "trample: boolean expected";
        return null;
    };

    /**
     * Creates an AssignDamage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof AssignDamage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {AssignDamage} AssignDamage
     */
    AssignDamage.fromObject = function fromObject(object) {
        if (object instanceof $root.AssignDamage)
            return object;
        var message = new $root.AssignDamage();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".AssignDamage.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.damageSource != null)
            message.damageSource = String(object.damageSource);
        if (object.possibleTargets) {
            if (!Array.isArray(object.possibleTargets))
                throw TypeError(".AssignDamage.possibleTargets: array expected");
            message.possibleTargets = [];
            for (var i = 0; i < object.possibleTargets.length; ++i)
                message.possibleTargets[i] = String(object.possibleTargets[i]);
        }
        if (object.totalDamage != null)
            message.totalDamage = object.totalDamage | 0;
        if (object.trample != null)
            message.trample = Boolean(object.trample);
        return message;
    };

    /**
     * Creates a plain object from an AssignDamage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof AssignDamage
     * @static
     * @param {AssignDamage} message AssignDamage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    AssignDamage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.possibleTargets = [];
        if (options.defaults) {
            object.game = null;
            object.damageSource = "";
            object.totalDamage = 0;
            object.trample = false;
        }
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.damageSource != null && message.hasOwnProperty("damageSource"))
            object.damageSource = message.damageSource;
        if (message.possibleTargets && message.possibleTargets.length) {
            object.possibleTargets = [];
            for (var j = 0; j < message.possibleTargets.length; ++j)
                object.possibleTargets[j] = message.possibleTargets[j];
        }
        if (message.totalDamage != null && message.hasOwnProperty("totalDamage"))
            object.totalDamage = message.totalDamage;
        if (message.trample != null && message.hasOwnProperty("trample"))
            object.trample = message.trample;
        return object;
    };

    /**
     * Converts this AssignDamage to JSON.
     * @function toJSON
     * @memberof AssignDamage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    AssignDamage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return AssignDamage;
})();

$root.SelectKnowledge = (function() {

    /**
     * Properties of a SelectKnowledge.
     * @exports ISelectKnowledge
     * @interface ISelectKnowledge
     * @property {IGameState|null} [game] SelectKnowledge game
     * @property {Array.<Knowledge>|null} [knowledgeList] SelectKnowledge knowledgeList
     */

    /**
     * Constructs a new SelectKnowledge.
     * @exports SelectKnowledge
     * @classdesc Represents a SelectKnowledge.
     * @implements ISelectKnowledge
     * @constructor
     * @param {ISelectKnowledge=} [properties] Properties to set
     */
    function SelectKnowledge(properties) {
        this.knowledgeList = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * SelectKnowledge game.
     * @member {IGameState|null|undefined} game
     * @memberof SelectKnowledge
     * @instance
     */
    SelectKnowledge.prototype.game = null;

    /**
     * SelectKnowledge knowledgeList.
     * @member {Array.<Knowledge>} knowledgeList
     * @memberof SelectKnowledge
     * @instance
     */
    SelectKnowledge.prototype.knowledgeList = $util.emptyArray;

    /**
     * Creates a new SelectKnowledge instance using the specified properties.
     * @function create
     * @memberof SelectKnowledge
     * @static
     * @param {ISelectKnowledge=} [properties] Properties to set
     * @returns {SelectKnowledge} SelectKnowledge instance
     */
    SelectKnowledge.create = function create(properties) {
        return new SelectKnowledge(properties);
    };

    /**
     * Encodes the specified SelectKnowledge message. Does not implicitly {@link SelectKnowledge.verify|verify} messages.
     * @function encode
     * @memberof SelectKnowledge
     * @static
     * @param {ISelectKnowledge} message SelectKnowledge message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectKnowledge.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        if (message.knowledgeList != null && message.knowledgeList.length) {
            writer.uint32(/* id 2, wireType 2 =*/18).fork();
            for (var i = 0; i < message.knowledgeList.length; ++i)
                writer.int32(message.knowledgeList[i]);
            writer.ldelim();
        }
        return writer;
    };

    /**
     * Encodes the specified SelectKnowledge message, length delimited. Does not implicitly {@link SelectKnowledge.verify|verify} messages.
     * @function encodeDelimited
     * @memberof SelectKnowledge
     * @static
     * @param {ISelectKnowledge} message SelectKnowledge message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    SelectKnowledge.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a SelectKnowledge message from the specified reader or buffer.
     * @function decode
     * @memberof SelectKnowledge
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {SelectKnowledge} SelectKnowledge
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectKnowledge.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.SelectKnowledge();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            case 2:
                if (!(message.knowledgeList && message.knowledgeList.length))
                    message.knowledgeList = [];
                if ((tag & 7) === 2) {
                    var end2 = reader.uint32() + reader.pos;
                    while (reader.pos < end2)
                        message.knowledgeList.push(reader.int32());
                } else
                    message.knowledgeList.push(reader.int32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a SelectKnowledge message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof SelectKnowledge
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {SelectKnowledge} SelectKnowledge
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    SelectKnowledge.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a SelectKnowledge message.
     * @function verify
     * @memberof SelectKnowledge
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    SelectKnowledge.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        if (message.knowledgeList != null && message.hasOwnProperty("knowledgeList")) {
            if (!Array.isArray(message.knowledgeList))
                return "knowledgeList: array expected";
            for (var i = 0; i < message.knowledgeList.length; ++i)
                switch (message.knowledgeList[i]) {
                default:
                    return "knowledgeList: enum value[] expected";
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                }
        }
        return null;
    };

    /**
     * Creates a SelectKnowledge message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof SelectKnowledge
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {SelectKnowledge} SelectKnowledge
     */
    SelectKnowledge.fromObject = function fromObject(object) {
        if (object instanceof $root.SelectKnowledge)
            return object;
        var message = new $root.SelectKnowledge();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".SelectKnowledge.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        if (object.knowledgeList) {
            if (!Array.isArray(object.knowledgeList))
                throw TypeError(".SelectKnowledge.knowledgeList: array expected");
            message.knowledgeList = [];
            for (var i = 0; i < object.knowledgeList.length; ++i)
                switch (object.knowledgeList[i]) {
                default:
                case "NONE":
                case 0:
                    message.knowledgeList[i] = 0;
                    break;
                case "PURPLE":
                case 1:
                    message.knowledgeList[i] = 1;
                    break;
                case "GREEN":
                case 2:
                    message.knowledgeList[i] = 2;
                    break;
                case "RED":
                case 3:
                    message.knowledgeList[i] = 3;
                    break;
                case "BLUE":
                case 4:
                    message.knowledgeList[i] = 4;
                    break;
                case "YELLOW":
                case 5:
                    message.knowledgeList[i] = 5;
                    break;
                }
        }
        return message;
    };

    /**
     * Creates a plain object from a SelectKnowledge message. Also converts values to other types if specified.
     * @function toObject
     * @memberof SelectKnowledge
     * @static
     * @param {SelectKnowledge} message SelectKnowledge
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    SelectKnowledge.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.knowledgeList = [];
        if (options.defaults)
            object.game = null;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        if (message.knowledgeList && message.knowledgeList.length) {
            object.knowledgeList = [];
            for (var j = 0; j < message.knowledgeList.length; ++j)
                object.knowledgeList[j] = options.enums === String ? $root.Knowledge[message.knowledgeList[j]] : message.knowledgeList[j];
        }
        return object;
    };

    /**
     * Converts this SelectKnowledge to JSON.
     * @function toJSON
     * @memberof SelectKnowledge
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    SelectKnowledge.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return SelectKnowledge;
})();

$root.PickCard = (function() {

    /**
     * Properties of a PickCard.
     * @exports IPickCard
     * @interface IPickCard
     * @property {IDraftState|null} [draft] PickCard draft
     * @property {Array.<ICard>|null} [candidates] PickCard candidates
     * @property {string|null} [message] PickCard message
     */

    /**
     * Constructs a new PickCard.
     * @exports PickCard
     * @classdesc Represents a PickCard.
     * @implements IPickCard
     * @constructor
     * @param {IPickCard=} [properties] Properties to set
     */
    function PickCard(properties) {
        this.candidates = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * PickCard draft.
     * @member {IDraftState|null|undefined} draft
     * @memberof PickCard
     * @instance
     */
    PickCard.prototype.draft = null;

    /**
     * PickCard candidates.
     * @member {Array.<ICard>} candidates
     * @memberof PickCard
     * @instance
     */
    PickCard.prototype.candidates = $util.emptyArray;

    /**
     * PickCard message.
     * @member {string} message
     * @memberof PickCard
     * @instance
     */
    PickCard.prototype.message = "";

    /**
     * Creates a new PickCard instance using the specified properties.
     * @function create
     * @memberof PickCard
     * @static
     * @param {IPickCard=} [properties] Properties to set
     * @returns {PickCard} PickCard instance
     */
    PickCard.create = function create(properties) {
        return new PickCard(properties);
    };

    /**
     * Encodes the specified PickCard message. Does not implicitly {@link PickCard.verify|verify} messages.
     * @function encode
     * @memberof PickCard
     * @static
     * @param {IPickCard} message PickCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PickCard.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.draft != null && Object.hasOwnProperty.call(message, "draft"))
            $root.DraftState.encode(message.draft, writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        if (message.candidates != null && message.candidates.length)
            for (var i = 0; i < message.candidates.length; ++i)
                $root.Card.encode(message.candidates[i], writer.uint32(/* id 4, wireType 2 =*/34).fork()).ldelim();
        if (message.message != null && Object.hasOwnProperty.call(message, "message"))
            writer.uint32(/* id 7, wireType 2 =*/58).string(message.message);
        return writer;
    };

    /**
     * Encodes the specified PickCard message, length delimited. Does not implicitly {@link PickCard.verify|verify} messages.
     * @function encodeDelimited
     * @memberof PickCard
     * @static
     * @param {IPickCard} message PickCard message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    PickCard.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a PickCard message from the specified reader or buffer.
     * @function decode
     * @memberof PickCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {PickCard} PickCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PickCard.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.PickCard();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 2:
                message.draft = $root.DraftState.decode(reader, reader.uint32());
                break;
            case 4:
                if (!(message.candidates && message.candidates.length))
                    message.candidates = [];
                message.candidates.push($root.Card.decode(reader, reader.uint32()));
                break;
            case 7:
                message.message = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a PickCard message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof PickCard
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {PickCard} PickCard
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    PickCard.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a PickCard message.
     * @function verify
     * @memberof PickCard
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    PickCard.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.draft != null && message.hasOwnProperty("draft")) {
            var error = $root.DraftState.verify(message.draft);
            if (error)
                return "draft." + error;
        }
        if (message.candidates != null && message.hasOwnProperty("candidates")) {
            if (!Array.isArray(message.candidates))
                return "candidates: array expected";
            for (var i = 0; i < message.candidates.length; ++i) {
                var error = $root.Card.verify(message.candidates[i]);
                if (error)
                    return "candidates." + error;
            }
        }
        if (message.message != null && message.hasOwnProperty("message"))
            if (!$util.isString(message.message))
                return "message: string expected";
        return null;
    };

    /**
     * Creates a PickCard message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof PickCard
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {PickCard} PickCard
     */
    PickCard.fromObject = function fromObject(object) {
        if (object instanceof $root.PickCard)
            return object;
        var message = new $root.PickCard();
        if (object.draft != null) {
            if (typeof object.draft !== "object")
                throw TypeError(".PickCard.draft: object expected");
            message.draft = $root.DraftState.fromObject(object.draft);
        }
        if (object.candidates) {
            if (!Array.isArray(object.candidates))
                throw TypeError(".PickCard.candidates: array expected");
            message.candidates = [];
            for (var i = 0; i < object.candidates.length; ++i) {
                if (typeof object.candidates[i] !== "object")
                    throw TypeError(".PickCard.candidates: object expected");
                message.candidates[i] = $root.Card.fromObject(object.candidates[i]);
            }
        }
        if (object.message != null)
            message.message = String(object.message);
        return message;
    };

    /**
     * Creates a plain object from a PickCard message. Also converts values to other types if specified.
     * @function toObject
     * @memberof PickCard
     * @static
     * @param {PickCard} message PickCard
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    PickCard.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.candidates = [];
        if (options.defaults) {
            object.draft = null;
            object.message = "";
        }
        if (message.draft != null && message.hasOwnProperty("draft"))
            object.draft = $root.DraftState.toObject(message.draft, options);
        if (message.candidates && message.candidates.length) {
            object.candidates = [];
            for (var j = 0; j < message.candidates.length; ++j)
                object.candidates[j] = $root.Card.toObject(message.candidates[j], options);
        }
        if (message.message != null && message.hasOwnProperty("message"))
            object.message = message.message;
        return object;
    };

    /**
     * Converts this PickCard to JSON.
     * @function toJSON
     * @memberof PickCard
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    PickCard.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return PickCard;
})();

$root.ServerGameMessage = (function() {

    /**
     * Properties of a ServerGameMessage.
     * @exports IServerGameMessage
     * @interface IServerGameMessage
     * @property {IUpdateGameState|null} [updateGameState] ServerGameMessage updateGameState
     * @property {IGameEnd|null} [gameEnd] ServerGameMessage gameEnd
     * @property {IOrderCards|null} [orderCards] ServerGameMessage orderCards
     * @property {ISelectFrom|null} [selectFrom] ServerGameMessage selectFrom
     * @property {ISelectXValue|null} [selectXValue] ServerGameMessage selectXValue
     * @property {ISelectAttackers|null} [selectAttackers] ServerGameMessage selectAttackers
     * @property {ISelectBlockers|null} [selectBlockers] ServerGameMessage selectBlockers
     * @property {IAssignDamage|null} [assignDamage] ServerGameMessage assignDamage
     * @property {ISelectKnowledge|null} [selectKnowledge] ServerGameMessage selectKnowledge
     * @property {IPickCard|null} [pickCard] ServerGameMessage pickCard
     */

    /**
     * Constructs a new ServerGameMessage.
     * @exports ServerGameMessage
     * @classdesc Represents a ServerGameMessage.
     * @implements IServerGameMessage
     * @constructor
     * @param {IServerGameMessage=} [properties] Properties to set
     */
    function ServerGameMessage(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * ServerGameMessage updateGameState.
     * @member {IUpdateGameState|null|undefined} updateGameState
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.updateGameState = null;

    /**
     * ServerGameMessage gameEnd.
     * @member {IGameEnd|null|undefined} gameEnd
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.gameEnd = null;

    /**
     * ServerGameMessage orderCards.
     * @member {IOrderCards|null|undefined} orderCards
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.orderCards = null;

    /**
     * ServerGameMessage selectFrom.
     * @member {ISelectFrom|null|undefined} selectFrom
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.selectFrom = null;

    /**
     * ServerGameMessage selectXValue.
     * @member {ISelectXValue|null|undefined} selectXValue
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.selectXValue = null;

    /**
     * ServerGameMessage selectAttackers.
     * @member {ISelectAttackers|null|undefined} selectAttackers
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.selectAttackers = null;

    /**
     * ServerGameMessage selectBlockers.
     * @member {ISelectBlockers|null|undefined} selectBlockers
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.selectBlockers = null;

    /**
     * ServerGameMessage assignDamage.
     * @member {IAssignDamage|null|undefined} assignDamage
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.assignDamage = null;

    /**
     * ServerGameMessage selectKnowledge.
     * @member {ISelectKnowledge|null|undefined} selectKnowledge
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.selectKnowledge = null;

    /**
     * ServerGameMessage pickCard.
     * @member {IPickCard|null|undefined} pickCard
     * @memberof ServerGameMessage
     * @instance
     */
    ServerGameMessage.prototype.pickCard = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * ServerGameMessage payload.
     * @member {"updateGameState"|"gameEnd"|"orderCards"|"selectFrom"|"selectXValue"|"selectAttackers"|"selectBlockers"|"assignDamage"|"selectKnowledge"|"pickCard"|undefined} payload
     * @memberof ServerGameMessage
     * @instance
     */
    Object.defineProperty(ServerGameMessage.prototype, "payload", {
        get: $util.oneOfGetter($oneOfFields = ["updateGameState", "gameEnd", "orderCards", "selectFrom", "selectXValue", "selectAttackers", "selectBlockers", "assignDamage", "selectKnowledge", "pickCard"]),
        set: $util.oneOfSetter($oneOfFields)
    });

    /**
     * Creates a new ServerGameMessage instance using the specified properties.
     * @function create
     * @memberof ServerGameMessage
     * @static
     * @param {IServerGameMessage=} [properties] Properties to set
     * @returns {ServerGameMessage} ServerGameMessage instance
     */
    ServerGameMessage.create = function create(properties) {
        return new ServerGameMessage(properties);
    };

    /**
     * Encodes the specified ServerGameMessage message. Does not implicitly {@link ServerGameMessage.verify|verify} messages.
     * @function encode
     * @memberof ServerGameMessage
     * @static
     * @param {IServerGameMessage} message ServerGameMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ServerGameMessage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.updateGameState != null && Object.hasOwnProperty.call(message, "updateGameState"))
            $root.UpdateGameState.encode(message.updateGameState, writer.uint32(/* id 8, wireType 2 =*/66).fork()).ldelim();
        if (message.gameEnd != null && Object.hasOwnProperty.call(message, "gameEnd"))
            $root.GameEnd.encode(message.gameEnd, writer.uint32(/* id 10, wireType 2 =*/82).fork()).ldelim();
        if (message.orderCards != null && Object.hasOwnProperty.call(message, "orderCards"))
            $root.OrderCards.encode(message.orderCards, writer.uint32(/* id 12, wireType 2 =*/98).fork()).ldelim();
        if (message.selectFrom != null && Object.hasOwnProperty.call(message, "selectFrom"))
            $root.SelectFrom.encode(message.selectFrom, writer.uint32(/* id 13, wireType 2 =*/106).fork()).ldelim();
        if (message.selectXValue != null && Object.hasOwnProperty.call(message, "selectXValue"))
            $root.SelectXValue.encode(message.selectXValue, writer.uint32(/* id 19, wireType 2 =*/154).fork()).ldelim();
        if (message.selectAttackers != null && Object.hasOwnProperty.call(message, "selectAttackers"))
            $root.SelectAttackers.encode(message.selectAttackers, writer.uint32(/* id 20, wireType 2 =*/162).fork()).ldelim();
        if (message.selectBlockers != null && Object.hasOwnProperty.call(message, "selectBlockers"))
            $root.SelectBlockers.encode(message.selectBlockers, writer.uint32(/* id 21, wireType 2 =*/170).fork()).ldelim();
        if (message.assignDamage != null && Object.hasOwnProperty.call(message, "assignDamage"))
            $root.AssignDamage.encode(message.assignDamage, writer.uint32(/* id 22, wireType 2 =*/178).fork()).ldelim();
        if (message.selectKnowledge != null && Object.hasOwnProperty.call(message, "selectKnowledge"))
            $root.SelectKnowledge.encode(message.selectKnowledge, writer.uint32(/* id 23, wireType 2 =*/186).fork()).ldelim();
        if (message.pickCard != null && Object.hasOwnProperty.call(message, "pickCard"))
            $root.PickCard.encode(message.pickCard, writer.uint32(/* id 24, wireType 2 =*/194).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified ServerGameMessage message, length delimited. Does not implicitly {@link ServerGameMessage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof ServerGameMessage
     * @static
     * @param {IServerGameMessage} message ServerGameMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ServerGameMessage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a ServerGameMessage message from the specified reader or buffer.
     * @function decode
     * @memberof ServerGameMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {ServerGameMessage} ServerGameMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ServerGameMessage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.ServerGameMessage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 8:
                message.updateGameState = $root.UpdateGameState.decode(reader, reader.uint32());
                break;
            case 10:
                message.gameEnd = $root.GameEnd.decode(reader, reader.uint32());
                break;
            case 12:
                message.orderCards = $root.OrderCards.decode(reader, reader.uint32());
                break;
            case 13:
                message.selectFrom = $root.SelectFrom.decode(reader, reader.uint32());
                break;
            case 19:
                message.selectXValue = $root.SelectXValue.decode(reader, reader.uint32());
                break;
            case 20:
                message.selectAttackers = $root.SelectAttackers.decode(reader, reader.uint32());
                break;
            case 21:
                message.selectBlockers = $root.SelectBlockers.decode(reader, reader.uint32());
                break;
            case 22:
                message.assignDamage = $root.AssignDamage.decode(reader, reader.uint32());
                break;
            case 23:
                message.selectKnowledge = $root.SelectKnowledge.decode(reader, reader.uint32());
                break;
            case 24:
                message.pickCard = $root.PickCard.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a ServerGameMessage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof ServerGameMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {ServerGameMessage} ServerGameMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ServerGameMessage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a ServerGameMessage message.
     * @function verify
     * @memberof ServerGameMessage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    ServerGameMessage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        var properties = {};
        if (message.updateGameState != null && message.hasOwnProperty("updateGameState")) {
            properties.payload = 1;
            {
                var error = $root.UpdateGameState.verify(message.updateGameState);
                if (error)
                    return "updateGameState." + error;
            }
        }
        if (message.gameEnd != null && message.hasOwnProperty("gameEnd")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.GameEnd.verify(message.gameEnd);
                if (error)
                    return "gameEnd." + error;
            }
        }
        if (message.orderCards != null && message.hasOwnProperty("orderCards")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.OrderCards.verify(message.orderCards);
                if (error)
                    return "orderCards." + error;
            }
        }
        if (message.selectFrom != null && message.hasOwnProperty("selectFrom")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectFrom.verify(message.selectFrom);
                if (error)
                    return "selectFrom." + error;
            }
        }
        if (message.selectXValue != null && message.hasOwnProperty("selectXValue")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectXValue.verify(message.selectXValue);
                if (error)
                    return "selectXValue." + error;
            }
        }
        if (message.selectAttackers != null && message.hasOwnProperty("selectAttackers")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectAttackers.verify(message.selectAttackers);
                if (error)
                    return "selectAttackers." + error;
            }
        }
        if (message.selectBlockers != null && message.hasOwnProperty("selectBlockers")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectBlockers.verify(message.selectBlockers);
                if (error)
                    return "selectBlockers." + error;
            }
        }
        if (message.assignDamage != null && message.hasOwnProperty("assignDamage")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.AssignDamage.verify(message.assignDamage);
                if (error)
                    return "assignDamage." + error;
            }
        }
        if (message.selectKnowledge != null && message.hasOwnProperty("selectKnowledge")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.SelectKnowledge.verify(message.selectKnowledge);
                if (error)
                    return "selectKnowledge." + error;
            }
        }
        if (message.pickCard != null && message.hasOwnProperty("pickCard")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.PickCard.verify(message.pickCard);
                if (error)
                    return "pickCard." + error;
            }
        }
        return null;
    };

    /**
     * Creates a ServerGameMessage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof ServerGameMessage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {ServerGameMessage} ServerGameMessage
     */
    ServerGameMessage.fromObject = function fromObject(object) {
        if (object instanceof $root.ServerGameMessage)
            return object;
        var message = new $root.ServerGameMessage();
        if (object.updateGameState != null) {
            if (typeof object.updateGameState !== "object")
                throw TypeError(".ServerGameMessage.updateGameState: object expected");
            message.updateGameState = $root.UpdateGameState.fromObject(object.updateGameState);
        }
        if (object.gameEnd != null) {
            if (typeof object.gameEnd !== "object")
                throw TypeError(".ServerGameMessage.gameEnd: object expected");
            message.gameEnd = $root.GameEnd.fromObject(object.gameEnd);
        }
        if (object.orderCards != null) {
            if (typeof object.orderCards !== "object")
                throw TypeError(".ServerGameMessage.orderCards: object expected");
            message.orderCards = $root.OrderCards.fromObject(object.orderCards);
        }
        if (object.selectFrom != null) {
            if (typeof object.selectFrom !== "object")
                throw TypeError(".ServerGameMessage.selectFrom: object expected");
            message.selectFrom = $root.SelectFrom.fromObject(object.selectFrom);
        }
        if (object.selectXValue != null) {
            if (typeof object.selectXValue !== "object")
                throw TypeError(".ServerGameMessage.selectXValue: object expected");
            message.selectXValue = $root.SelectXValue.fromObject(object.selectXValue);
        }
        if (object.selectAttackers != null) {
            if (typeof object.selectAttackers !== "object")
                throw TypeError(".ServerGameMessage.selectAttackers: object expected");
            message.selectAttackers = $root.SelectAttackers.fromObject(object.selectAttackers);
        }
        if (object.selectBlockers != null) {
            if (typeof object.selectBlockers !== "object")
                throw TypeError(".ServerGameMessage.selectBlockers: object expected");
            message.selectBlockers = $root.SelectBlockers.fromObject(object.selectBlockers);
        }
        if (object.assignDamage != null) {
            if (typeof object.assignDamage !== "object")
                throw TypeError(".ServerGameMessage.assignDamage: object expected");
            message.assignDamage = $root.AssignDamage.fromObject(object.assignDamage);
        }
        if (object.selectKnowledge != null) {
            if (typeof object.selectKnowledge !== "object")
                throw TypeError(".ServerGameMessage.selectKnowledge: object expected");
            message.selectKnowledge = $root.SelectKnowledge.fromObject(object.selectKnowledge);
        }
        if (object.pickCard != null) {
            if (typeof object.pickCard !== "object")
                throw TypeError(".ServerGameMessage.pickCard: object expected");
            message.pickCard = $root.PickCard.fromObject(object.pickCard);
        }
        return message;
    };

    /**
     * Creates a plain object from a ServerGameMessage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof ServerGameMessage
     * @static
     * @param {ServerGameMessage} message ServerGameMessage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    ServerGameMessage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (message.updateGameState != null && message.hasOwnProperty("updateGameState")) {
            object.updateGameState = $root.UpdateGameState.toObject(message.updateGameState, options);
            if (options.oneofs)
                object.payload = "updateGameState";
        }
        if (message.gameEnd != null && message.hasOwnProperty("gameEnd")) {
            object.gameEnd = $root.GameEnd.toObject(message.gameEnd, options);
            if (options.oneofs)
                object.payload = "gameEnd";
        }
        if (message.orderCards != null && message.hasOwnProperty("orderCards")) {
            object.orderCards = $root.OrderCards.toObject(message.orderCards, options);
            if (options.oneofs)
                object.payload = "orderCards";
        }
        if (message.selectFrom != null && message.hasOwnProperty("selectFrom")) {
            object.selectFrom = $root.SelectFrom.toObject(message.selectFrom, options);
            if (options.oneofs)
                object.payload = "selectFrom";
        }
        if (message.selectXValue != null && message.hasOwnProperty("selectXValue")) {
            object.selectXValue = $root.SelectXValue.toObject(message.selectXValue, options);
            if (options.oneofs)
                object.payload = "selectXValue";
        }
        if (message.selectAttackers != null && message.hasOwnProperty("selectAttackers")) {
            object.selectAttackers = $root.SelectAttackers.toObject(message.selectAttackers, options);
            if (options.oneofs)
                object.payload = "selectAttackers";
        }
        if (message.selectBlockers != null && message.hasOwnProperty("selectBlockers")) {
            object.selectBlockers = $root.SelectBlockers.toObject(message.selectBlockers, options);
            if (options.oneofs)
                object.payload = "selectBlockers";
        }
        if (message.assignDamage != null && message.hasOwnProperty("assignDamage")) {
            object.assignDamage = $root.AssignDamage.toObject(message.assignDamage, options);
            if (options.oneofs)
                object.payload = "assignDamage";
        }
        if (message.selectKnowledge != null && message.hasOwnProperty("selectKnowledge")) {
            object.selectKnowledge = $root.SelectKnowledge.toObject(message.selectKnowledge, options);
            if (options.oneofs)
                object.payload = "selectKnowledge";
        }
        if (message.pickCard != null && message.hasOwnProperty("pickCard")) {
            object.pickCard = $root.PickCard.toObject(message.pickCard, options);
            if (options.oneofs)
                object.payload = "pickCard";
        }
        return object;
    };

    /**
     * Converts this ServerGameMessage to JSON.
     * @function toJSON
     * @memberof ServerGameMessage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    ServerGameMessage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return ServerGameMessage;
})();

$root.LoginResponse = (function() {

    /**
     * Properties of a LoginResponse.
     * @exports ILoginResponse
     * @interface ILoginResponse
     * @property {string|null} [gameId] LoginResponse gameId
     * @property {string|null} [playerId] LoginResponse playerId
     */

    /**
     * Constructs a new LoginResponse.
     * @exports LoginResponse
     * @classdesc Messages from server to client.
     * @implements ILoginResponse
     * @constructor
     * @param {ILoginResponse=} [properties] Properties to set
     */
    function LoginResponse(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * LoginResponse gameId.
     * @member {string} gameId
     * @memberof LoginResponse
     * @instance
     */
    LoginResponse.prototype.gameId = "";

    /**
     * LoginResponse playerId.
     * @member {string} playerId
     * @memberof LoginResponse
     * @instance
     */
    LoginResponse.prototype.playerId = "";

    /**
     * Creates a new LoginResponse instance using the specified properties.
     * @function create
     * @memberof LoginResponse
     * @static
     * @param {ILoginResponse=} [properties] Properties to set
     * @returns {LoginResponse} LoginResponse instance
     */
    LoginResponse.create = function create(properties) {
        return new LoginResponse(properties);
    };

    /**
     * Encodes the specified LoginResponse message. Does not implicitly {@link LoginResponse.verify|verify} messages.
     * @function encode
     * @memberof LoginResponse
     * @static
     * @param {ILoginResponse} message LoginResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    LoginResponse.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.gameId != null && Object.hasOwnProperty.call(message, "gameId"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.gameId);
        if (message.playerId != null && Object.hasOwnProperty.call(message, "playerId"))
            writer.uint32(/* id 2, wireType 2 =*/18).string(message.playerId);
        return writer;
    };

    /**
     * Encodes the specified LoginResponse message, length delimited. Does not implicitly {@link LoginResponse.verify|verify} messages.
     * @function encodeDelimited
     * @memberof LoginResponse
     * @static
     * @param {ILoginResponse} message LoginResponse message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    LoginResponse.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a LoginResponse message from the specified reader or buffer.
     * @function decode
     * @memberof LoginResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {LoginResponse} LoginResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    LoginResponse.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.LoginResponse();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.gameId = reader.string();
                break;
            case 2:
                message.playerId = reader.string();
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a LoginResponse message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof LoginResponse
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {LoginResponse} LoginResponse
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    LoginResponse.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a LoginResponse message.
     * @function verify
     * @memberof LoginResponse
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    LoginResponse.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.gameId != null && message.hasOwnProperty("gameId"))
            if (!$util.isString(message.gameId))
                return "gameId: string expected";
        if (message.playerId != null && message.hasOwnProperty("playerId"))
            if (!$util.isString(message.playerId))
                return "playerId: string expected";
        return null;
    };

    /**
     * Creates a LoginResponse message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof LoginResponse
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {LoginResponse} LoginResponse
     */
    LoginResponse.fromObject = function fromObject(object) {
        if (object instanceof $root.LoginResponse)
            return object;
        var message = new $root.LoginResponse();
        if (object.gameId != null)
            message.gameId = String(object.gameId);
        if (object.playerId != null)
            message.playerId = String(object.playerId);
        return message;
    };

    /**
     * Creates a plain object from a LoginResponse message. Also converts values to other types if specified.
     * @function toObject
     * @memberof LoginResponse
     * @static
     * @param {LoginResponse} message LoginResponse
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    LoginResponse.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.gameId = "";
            object.playerId = "";
        }
        if (message.gameId != null && message.hasOwnProperty("gameId"))
            object.gameId = message.gameId;
        if (message.playerId != null && message.hasOwnProperty("playerId"))
            object.playerId = message.playerId;
        return object;
    };

    /**
     * Converts this LoginResponse to JSON.
     * @function toJSON
     * @memberof LoginResponse
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    LoginResponse.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return LoginResponse;
})();

$root.NewGame = (function() {

    /**
     * Properties of a NewGame.
     * @exports INewGame
     * @interface INewGame
     * @property {IGameState|null} [game] NewGame game
     */

    /**
     * Constructs a new NewGame.
     * @exports NewGame
     * @classdesc Represents a NewGame.
     * @implements INewGame
     * @constructor
     * @param {INewGame=} [properties] Properties to set
     */
    function NewGame(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * NewGame game.
     * @member {IGameState|null|undefined} game
     * @memberof NewGame
     * @instance
     */
    NewGame.prototype.game = null;

    /**
     * Creates a new NewGame instance using the specified properties.
     * @function create
     * @memberof NewGame
     * @static
     * @param {INewGame=} [properties] Properties to set
     * @returns {NewGame} NewGame instance
     */
    NewGame.create = function create(properties) {
        return new NewGame(properties);
    };

    /**
     * Encodes the specified NewGame message. Does not implicitly {@link NewGame.verify|verify} messages.
     * @function encode
     * @memberof NewGame
     * @static
     * @param {INewGame} message NewGame message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NewGame.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.game != null && Object.hasOwnProperty.call(message, "game"))
            $root.GameState.encode(message.game, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified NewGame message, length delimited. Does not implicitly {@link NewGame.verify|verify} messages.
     * @function encodeDelimited
     * @memberof NewGame
     * @static
     * @param {INewGame} message NewGame message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NewGame.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a NewGame message from the specified reader or buffer.
     * @function decode
     * @memberof NewGame
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {NewGame} NewGame
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NewGame.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.NewGame();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.game = $root.GameState.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a NewGame message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof NewGame
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {NewGame} NewGame
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NewGame.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a NewGame message.
     * @function verify
     * @memberof NewGame
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    NewGame.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.game != null && message.hasOwnProperty("game")) {
            var error = $root.GameState.verify(message.game);
            if (error)
                return "game." + error;
        }
        return null;
    };

    /**
     * Creates a NewGame message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof NewGame
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {NewGame} NewGame
     */
    NewGame.fromObject = function fromObject(object) {
        if (object instanceof $root.NewGame)
            return object;
        var message = new $root.NewGame();
        if (object.game != null) {
            if (typeof object.game !== "object")
                throw TypeError(".NewGame.game: object expected");
            message.game = $root.GameState.fromObject(object.game);
        }
        return message;
    };

    /**
     * Creates a plain object from a NewGame message. Also converts values to other types if specified.
     * @function toObject
     * @memberof NewGame
     * @static
     * @param {NewGame} message NewGame
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    NewGame.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.game = null;
        if (message.game != null && message.hasOwnProperty("game"))
            object.game = $root.GameState.toObject(message.game, options);
        return object;
    };

    /**
     * Converts this NewGame to JSON.
     * @function toJSON
     * @memberof NewGame
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    NewGame.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return NewGame;
})();

$root.NewDraft = (function() {

    /**
     * Properties of a NewDraft.
     * @exports INewDraft
     * @interface INewDraft
     * @property {IDraftState|null} [draft] NewDraft draft
     */

    /**
     * Constructs a new NewDraft.
     * @exports NewDraft
     * @classdesc Represents a NewDraft.
     * @implements INewDraft
     * @constructor
     * @param {INewDraft=} [properties] Properties to set
     */
    function NewDraft(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * NewDraft draft.
     * @member {IDraftState|null|undefined} draft
     * @memberof NewDraft
     * @instance
     */
    NewDraft.prototype.draft = null;

    /**
     * Creates a new NewDraft instance using the specified properties.
     * @function create
     * @memberof NewDraft
     * @static
     * @param {INewDraft=} [properties] Properties to set
     * @returns {NewDraft} NewDraft instance
     */
    NewDraft.create = function create(properties) {
        return new NewDraft(properties);
    };

    /**
     * Encodes the specified NewDraft message. Does not implicitly {@link NewDraft.verify|verify} messages.
     * @function encode
     * @memberof NewDraft
     * @static
     * @param {INewDraft} message NewDraft message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NewDraft.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.draft != null && Object.hasOwnProperty.call(message, "draft"))
            $root.DraftState.encode(message.draft, writer.uint32(/* id 1, wireType 2 =*/10).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified NewDraft message, length delimited. Does not implicitly {@link NewDraft.verify|verify} messages.
     * @function encodeDelimited
     * @memberof NewDraft
     * @static
     * @param {INewDraft} message NewDraft message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NewDraft.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a NewDraft message from the specified reader or buffer.
     * @function decode
     * @memberof NewDraft
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {NewDraft} NewDraft
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NewDraft.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.NewDraft();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 1:
                message.draft = $root.DraftState.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a NewDraft message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof NewDraft
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {NewDraft} NewDraft
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NewDraft.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a NewDraft message.
     * @function verify
     * @memberof NewDraft
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    NewDraft.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.draft != null && message.hasOwnProperty("draft")) {
            var error = $root.DraftState.verify(message.draft);
            if (error)
                return "draft." + error;
        }
        return null;
    };

    /**
     * Creates a NewDraft message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof NewDraft
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {NewDraft} NewDraft
     */
    NewDraft.fromObject = function fromObject(object) {
        if (object instanceof $root.NewDraft)
            return object;
        var message = new $root.NewDraft();
        if (object.draft != null) {
            if (typeof object.draft !== "object")
                throw TypeError(".NewDraft.draft: object expected");
            message.draft = $root.DraftState.fromObject(object.draft);
        }
        return message;
    };

    /**
     * Creates a plain object from a NewDraft message. Also converts values to other types if specified.
     * @function toObject
     * @memberof NewDraft
     * @static
     * @param {NewDraft} message NewDraft
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    NewDraft.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults)
            object.draft = null;
        if (message.draft != null && message.hasOwnProperty("draft"))
            object.draft = $root.DraftState.toObject(message.draft, options);
        return object;
    };

    /**
     * Converts this NewDraft to JSON.
     * @function toJSON
     * @memberof NewDraft
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    NewDraft.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return NewDraft;
})();

$root.ServerMessage = (function() {

    /**
     * Properties of a ServerMessage.
     * @exports IServerMessage
     * @interface IServerMessage
     * @property {ILoginResponse|null} [loginResponse] ServerMessage loginResponse
     * @property {INewGame|null} [newGame] ServerMessage newGame
     * @property {INewDraft|null} [newDraft] ServerMessage newDraft
     */

    /**
     * Constructs a new ServerMessage.
     * @exports ServerMessage
     * @classdesc Represents a ServerMessage.
     * @implements IServerMessage
     * @constructor
     * @param {IServerMessage=} [properties] Properties to set
     */
    function ServerMessage(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * ServerMessage loginResponse.
     * @member {ILoginResponse|null|undefined} loginResponse
     * @memberof ServerMessage
     * @instance
     */
    ServerMessage.prototype.loginResponse = null;

    /**
     * ServerMessage newGame.
     * @member {INewGame|null|undefined} newGame
     * @memberof ServerMessage
     * @instance
     */
    ServerMessage.prototype.newGame = null;

    /**
     * ServerMessage newDraft.
     * @member {INewDraft|null|undefined} newDraft
     * @memberof ServerMessage
     * @instance
     */
    ServerMessage.prototype.newDraft = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * ServerMessage payload.
     * @member {"loginResponse"|"newGame"|"newDraft"|undefined} payload
     * @memberof ServerMessage
     * @instance
     */
    Object.defineProperty(ServerMessage.prototype, "payload", {
        get: $util.oneOfGetter($oneOfFields = ["loginResponse", "newGame", "newDraft"]),
        set: $util.oneOfSetter($oneOfFields)
    });

    /**
     * Creates a new ServerMessage instance using the specified properties.
     * @function create
     * @memberof ServerMessage
     * @static
     * @param {IServerMessage=} [properties] Properties to set
     * @returns {ServerMessage} ServerMessage instance
     */
    ServerMessage.create = function create(properties) {
        return new ServerMessage(properties);
    };

    /**
     * Encodes the specified ServerMessage message. Does not implicitly {@link ServerMessage.verify|verify} messages.
     * @function encode
     * @memberof ServerMessage
     * @static
     * @param {IServerMessage} message ServerMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ServerMessage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.loginResponse != null && Object.hasOwnProperty.call(message, "loginResponse"))
            $root.LoginResponse.encode(message.loginResponse, writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
        if (message.newGame != null && Object.hasOwnProperty.call(message, "newGame"))
            $root.NewGame.encode(message.newGame, writer.uint32(/* id 7, wireType 2 =*/58).fork()).ldelim();
        if (message.newDraft != null && Object.hasOwnProperty.call(message, "newDraft"))
            $root.NewDraft.encode(message.newDraft, writer.uint32(/* id 8, wireType 2 =*/66).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified ServerMessage message, length delimited. Does not implicitly {@link ServerMessage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof ServerMessage
     * @static
     * @param {IServerMessage} message ServerMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    ServerMessage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a ServerMessage message from the specified reader or buffer.
     * @function decode
     * @memberof ServerMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {ServerMessage} ServerMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ServerMessage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.ServerMessage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
            case 2:
                message.loginResponse = $root.LoginResponse.decode(reader, reader.uint32());
                break;
            case 7:
                message.newGame = $root.NewGame.decode(reader, reader.uint32());
                break;
            case 8:
                message.newDraft = $root.NewDraft.decode(reader, reader.uint32());
                break;
            default:
                reader.skipType(tag & 7);
                break;
            }
        }
        return message;
    };

    /**
     * Decodes a ServerMessage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof ServerMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {ServerMessage} ServerMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    ServerMessage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a ServerMessage message.
     * @function verify
     * @memberof ServerMessage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    ServerMessage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        var properties = {};
        if (message.loginResponse != null && message.hasOwnProperty("loginResponse")) {
            properties.payload = 1;
            {
                var error = $root.LoginResponse.verify(message.loginResponse);
                if (error)
                    return "loginResponse." + error;
            }
        }
        if (message.newGame != null && message.hasOwnProperty("newGame")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.NewGame.verify(message.newGame);
                if (error)
                    return "newGame." + error;
            }
        }
        if (message.newDraft != null && message.hasOwnProperty("newDraft")) {
            if (properties.payload === 1)
                return "payload: multiple values";
            properties.payload = 1;
            {
                var error = $root.NewDraft.verify(message.newDraft);
                if (error)
                    return "newDraft." + error;
            }
        }
        return null;
    };

    /**
     * Creates a ServerMessage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof ServerMessage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {ServerMessage} ServerMessage
     */
    ServerMessage.fromObject = function fromObject(object) {
        if (object instanceof $root.ServerMessage)
            return object;
        var message = new $root.ServerMessage();
        if (object.loginResponse != null) {
            if (typeof object.loginResponse !== "object")
                throw TypeError(".ServerMessage.loginResponse: object expected");
            message.loginResponse = $root.LoginResponse.fromObject(object.loginResponse);
        }
        if (object.newGame != null) {
            if (typeof object.newGame !== "object")
                throw TypeError(".ServerMessage.newGame: object expected");
            message.newGame = $root.NewGame.fromObject(object.newGame);
        }
        if (object.newDraft != null) {
            if (typeof object.newDraft !== "object")
                throw TypeError(".ServerMessage.newDraft: object expected");
            message.newDraft = $root.NewDraft.fromObject(object.newDraft);
        }
        return message;
    };

    /**
     * Creates a plain object from a ServerMessage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof ServerMessage
     * @static
     * @param {ServerMessage} message ServerMessage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    ServerMessage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (message.loginResponse != null && message.hasOwnProperty("loginResponse")) {
            object.loginResponse = $root.LoginResponse.toObject(message.loginResponse, options);
            if (options.oneofs)
                object.payload = "loginResponse";
        }
        if (message.newGame != null && message.hasOwnProperty("newGame")) {
            object.newGame = $root.NewGame.toObject(message.newGame, options);
            if (options.oneofs)
                object.payload = "newGame";
        }
        if (message.newDraft != null && message.hasOwnProperty("newDraft")) {
            object.newDraft = $root.NewDraft.toObject(message.newDraft, options);
            if (options.oneofs)
                object.payload = "newDraft";
        }
        return object;
    };

    /**
     * Converts this ServerMessage to JSON.
     * @function toJSON
     * @memberof ServerMessage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    ServerMessage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return ServerMessage;
})();

module.exports = $root;
