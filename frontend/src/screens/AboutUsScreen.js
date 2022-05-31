import React from 'react'
import About from '../images/About.JPG'

function AboutUsScreen() {
    return (
        <div className="about">
            <div className="about_image">
                <img src={About} alt="" />
                <div className="about_title">
                    Earth in pain. &nbsp; <br/> <span style={{color:"rgba(255,255,255,0.9)"}}>Are you gonna do nothing?</span>
                </div>
                <div className="about_description">
                    Just be with us.<br/>
                    Just be a part of us.<br/>
                    You can be one of the earth savers.
                </div>
            </div>
        </div>
    )
}

export default AboutUsScreen
